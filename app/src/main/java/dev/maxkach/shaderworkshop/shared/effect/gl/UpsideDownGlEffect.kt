package dev.maxkach.shaderworkshop.shared.effect.gl

import android.content.Context
import android.opengl.GLES20
import androidx.media3.common.VideoFrameProcessingException
import androidx.media3.common.util.GlProgram
import androidx.media3.common.util.GlUtil
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.BaseGlShaderProgram
import androidx.media3.effect.GlEffect
import androidx.media3.effect.GlShaderProgram
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState

@UnstableApi
class UpsideDownGlEffect(
  private val state: UpsideDownEffectState,
) : GlEffect {

  override fun toGlShaderProgram(context: Context, useHdr: Boolean): GlShaderProgram {
    return UpsideDownShaderProgram(state, useHdr)
  }
}

@UnstableApi
private class UpsideDownShaderProgram(
  private val state: UpsideDownEffectState,
  useHdr: Boolean,
) : BaseGlShaderProgram(useHdr, /* texturePoolCapacity= */ 1) {

  private val glProgram: GlProgram = try {
    GlProgram(UPSIDE_DOWN_VERTEX_SHADER, UPSIDE_DOWN_FRAGMENT_SHADER_2D)
  } catch (e: GlUtil.GlException) {
    throw VideoFrameProcessingException(e)
  }

  private var width = 1
  private var height = 1

  override fun configure(inputWidth: Int, inputHeight: Int): Size {
    width = inputWidth
    height = inputHeight
    return Size(inputWidth, inputHeight)
  }

  override fun drawFrame(inputTexId: Int, presentationTimeUs: Long) {

    val snapshot = state.glSnapshot

    try {
      glProgram.use()
      glProgram.setSamplerTexIdUniform("uTexSampler", inputTexId, /* texUnitIndex= */ 0)
      glProgram.setFloatsUniform("uResolution", floatArrayOf(width.toFloat(), height.toFloat()))
      glProgram.setFloatUniform("uGlitchIntensity", snapshot.glitchIntensity)
      glProgram.setFloatUniform("uUpsideDownAmount", snapshot.upsideDownAmount)

      glProgram.setFloatsUniformIfPresent("uTime", floatArrayOf(snapshot.timeSeconds))
      glProgram.setFloatUniform("uDarknessIntensity", snapshot.darknessIntensity)
      glProgram.setFloatUniform("uRealRandom", snapshot.realRandom)
      glProgram.setFloatUniform("uSlices", Slices)
      glProgram.setBufferAttribute(
          "aFramePosition",
          GlUtil.getNormalizedCoordinateBounds(),
          GlUtil.HOMOGENEOUS_COORDINATE_VECTOR_SIZE,
      )
      glProgram.bindAttributesAndUniforms()
      GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, /* first= */ 0, QuadVertexCount)
      GlUtil.checkGlError()
    } catch (e: GlUtil.GlException) {
      throw VideoFrameProcessingException(e, presentationTimeUs)
    }
  }

  override fun release() {
    super.release()
    try {
      glProgram.delete()
    } catch (e: GlUtil.GlException) {
      throw VideoFrameProcessingException(e)
    }
  }
}

private const val Slices = 70f
private const val QuadVertexCount = 4
