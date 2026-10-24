package dev.maxkach.shaderworkshop.exercise2.end.grading

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
fun Modifier.upsideDownGrading(state: UpsideDownEffectState): Modifier {
  val shader = remember { RuntimeShader(GRADING_SHADER) }
  if (state.snapshot.upsideDownAmount <= 0f) return this

  return clipToBounds().graphicsLayer {
    shader.setFloatUniform("darknessIntensity", state.snapshot.darknessIntensity)

    renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "image").asComposeRenderEffect()
  }
}
