package dev.maxkach.shaderworkshop.exercise2.end.agsl

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import dev.maxkach.shaderworkshop.shared.effect.ParticleCount
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.shared.effect.strangerVines

val isAgslSupported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

@Composable
fun Modifier.upsideDownAgsl(
  state: UpsideDownEffectState,
  particleCount: Int = ParticleCount,
  drawVines: Boolean = true,
): Modifier {
  if (!isAgslSupported) return this

  val snapshot = state.snapshot
  var modifier = this

  if (snapshot.glitchIntensity > 0f) {
    modifier = modifier.agslGlitch(state)
  }
  if (snapshot.upsideDownAmount > 0f) {
    modifier = modifier.agslUpsideDown(state, particleCount)
    if (drawVines) {
      modifier = modifier.strangerVines()
    }
  }

  return modifier
}

@Composable
fun Modifier.upsideDownVines(state: UpsideDownEffectState): Modifier {
  return if (state.snapshot.upsideDownAmount > 0f) {
    strangerVines(baseColor = GradedVineColor)
  } else {
    this
  }
}

private val GradedVineColor = Color(0xFF03060A)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Stable
@Composable
fun Modifier.agslGlitch(
  state: UpsideDownEffectState,
  slices: Int = DefaultSlices,
): Modifier {
  val shader = remember { RuntimeShader(GLITCH_SHADER) }

  return clipToBounds().graphicsLayer {
    val snapshot = state.snapshot
    shader.setIntUniform("slices", slices)
    shader.setFloatUniform("imageSize", size.width, size.height)
    shader.setFloatUniform("intensity", snapshot.glitchIntensity)
    shader.setFloatUniform("realRandom", snapshot.realRandom)

    renderEffect = shader.asRenderEffect()
  }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Stable
@Composable
fun Modifier.agslUpsideDown(
  state: UpsideDownEffectState,
  particleCount: Int = ParticleCount,
): Modifier {
  val shader = remember(particleCount) { RuntimeShader(upsideDownShader(particleCount)) }

  return clipToBounds().graphicsLayer {
    val snapshot = state.snapshot
    shader.setFloatUniform("imageSize", size.width, size.height)
    shader.setFloatUniform("time", snapshot.timeSeconds)
    shader.setFloatUniform("darknessIntensity", snapshot.darknessIntensity)

    renderEffect = shader.asRenderEffect()
  }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun RuntimeShader.asRenderEffect(): RenderEffect {
  return android.graphics.RenderEffect
      .createRuntimeShaderEffect(this, "image")
      .asComposeRenderEffect()
}

private const val DefaultSlices = 70
