package dev.maxkach.shaderworkshop.shared.effect.mesh

import android.graphics.BlendMode
import android.graphics.ColorSpace
import android.graphics.Mesh
import android.graphics.MeshSpecification
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import java.nio.ByteBuffer
import java.nio.ByteOrder
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.end.agsl.upsideDownVines
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions

val isMeshSupported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

@Composable
fun Modifier.meshVines(state: UpsideDownEffectState): Modifier {
  if (!isMeshSupported) return this
  return if (state.snapshot.upsideDownAmount > 0f) meshVinesOverlay() else this
}

@Composable
fun Modifier.upsideDownRouteVines(state: UpsideDownEffectState): Modifier =
    if (isMeshSupported) meshVines(state) else upsideDownVines(state)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Modifier.meshVinesOverlay(): Modifier {
  val capture = LocalCaptureOptions.current
  val cycle = remember { VinesCycle() }

  var progress by remember { mutableFloatStateOf(if (capture.fixedEffect) 1f else 0f) }
  var seed by remember { mutableStateOf(VinesCycle.BaseSeed) }

  if (!capture.fixedEffect) {
    LaunchedEffect(cycle) {
      while (true) {
        withFrameNanos { frameNanos ->
          progress = cycle.progress(frameNanos / NanosPerSecond)
          seed = cycle.seed
        }
      }
    }
  }

  val specification = remember { vinesMeshSpecification() }
  val paint = remember { Paint() }

  return drawWithCache {

    val meshes = buildVineMeshes(specification, seed, size)
    onDrawWithContent {
      drawContent()
      val visible = progress
      if (visible > 0f) {
        drawIntoCanvas { canvas ->
          for (vine in meshes) {
            vine.mesh.setFloatUniform("progress", visible)
            canvas.nativeCanvas.drawMesh(vine.mesh, BlendMode.SRC_OVER, paint)
          }
        }
      }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class VineMesh(val mesh: Mesh, @Suppress("unused") private val vertices: ByteBuffer)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun vinesMeshSpecification(): MeshSpecification = MeshSpecification.make(
    arrayOf(
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 0, "position"),
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 8, "prev"),
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 16, "next"),
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 24, "meta"),
    ),
    VineVertexStrideBytes,
    emptyArray(),
    VINES_VERTEX_SHADER,
    VINES_FRAGMENT_SHADER,
    ColorSpace.get(ColorSpace.Named.SRGB),
    MeshSpecification.ALPHA_TYPE_PREMULTIPLIED,
)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun buildVineMeshes(specification: MeshSpecification, seed: UInt, size: Size): List<VineMesh> {
  if (size.width <= 0f || size.height <= 0f) return emptyList()

  val bounds = RectF(-MainWidthPx, -MainWidthPx, size.width + MainWidthPx, size.height + MainWidthPx)
  return (0 until VinesCount).map { vine ->
    val line = walkVineCenterline(vine, seed, size.width, size.height)
    val vertices = packVineVertices(line)
    val buffer = ByteBuffer.allocateDirect(vertices.size * Float.SIZE_BYTES).order(ByteOrder.nativeOrder())
    buffer.asFloatBuffer().put(vertices)
    val mesh = Mesh(specification, Mesh.TRIANGLE_STRIP, buffer, line.count * 2, bounds).apply {
      setFloatUniform("screenSize", size.width, size.height)
      setFloatUniform("mainWidth", MainWidthPx)
      setFloatUniform("pointCount", line.count.toFloat())
      setFloatUniform("progress", 0f)
    }
    VineMesh(mesh, buffer)
  }
}

const val MainWidthPx = 80f
private const val NanosPerSecond = 1_000_000_000.0
