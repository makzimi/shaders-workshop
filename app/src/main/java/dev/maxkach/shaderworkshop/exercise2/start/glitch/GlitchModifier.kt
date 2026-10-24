package dev.maxkach.shaderworkshop.exercise2.start.glitch

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState

@Composable
fun Modifier.upsideDownGlitch(
  state: UpsideDownEffectState,
  slices: Int = DefaultSlices,
): Modifier {
  val shader = remember { RuntimeShader(GLITCH_SHADER) }
  if (state.snapshot.glitchIntensity <= 0f) return this

  return clipToBounds().graphicsLayer {
    val snapshot = state.snapshot
    shader.setIntUniform("slices", slices)
    shader.setFloatUniform("imageSize", size.width, size.height)
    shader.setFloatUniform("intensity", snapshot.glitchIntensity)
    shader.setFloatUniform("realRandom", snapshot.realRandom)

    renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "image").asComposeRenderEffect()
  }
}

private const val DefaultSlices = 70
