package dev.maxkach.shaderworkshop.exercise2.end.vines

import android.graphics.BlendMode
import android.graphics.Paint
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions

val isMeshSupported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

@Composable
fun Modifier.upsideDownVines(state: UpsideDownEffectState): Modifier {
  if (state.snapshot.upsideDownAmount <= 0f) return this
  return if (isMeshSupported) meshVinesOverlay() else strangerVines(baseColor = GradedVineColor)
}

private val GradedVineColor = Color(0xFF03060A)

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

private const val NanosPerSecond = 1_000_000_000.0
