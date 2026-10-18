package dev.maxkach.shaderworkshop.shared.effect.snow

import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ShaderBrush
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.end.agsl.isAgslSupported

@Composable
fun Modifier.upsideDownSnow(state: UpsideDownEffectState): Modifier {
  if (!isAgslSupported) return this
  val shader = remember { RuntimeShader(SNOW_SHADER) }
  val brush = remember(shader) { ShaderBrush(shader) }
  if (state.snapshot.upsideDownAmount <= 0f) return this

  return drawWithContent {
    drawContent()
    shader.setFloatUniform("size", size.width, size.height)
    shader.setFloatUniform("time", state.snapshot.timeSeconds)
    drawRect(brush)
  }
}
