package dev.maxkach.shaderworkshop.shared.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import kotlin.random.Random
import kotlinx.coroutines.delay
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions

data class EffectSnapshot(
  val glitchIntensity: Float = 0f,
  val upsideDownAmount: Float = 0f,
  val timeSeconds: Float = 0f,
  val darknessIntensity: Float = DefaultDarkness,
  val realRandom: Float = 0f,
) {
  val isIdle: Boolean get() = glitchIntensity <= 0f && upsideDownAmount <= 0f

  companion object {
    const val DefaultDarkness = 0.4f
  }
}

@Stable
class UpsideDownEffectState(initiallyActive: Boolean = false) {

  var isActive: Boolean by mutableStateOf(initiallyActive)
    private set

  var snapshot: EffectSnapshot by mutableStateOf(EffectSnapshot())
    private set

  @Volatile
  var glSnapshot: EffectSnapshot = EffectSnapshot()
    private set

  fun toggle() {
    isActive = !isActive
  }

  internal fun publish(value: EffectSnapshot) {
    snapshot = value
    glSnapshot = value
  }
}

@Composable
fun rememberUpsideDownEffectState(initiallyActive: Boolean = false): UpsideDownEffectState {
  val capture = LocalCaptureOptions.current
  val state = remember { UpsideDownEffectState(initiallyActive) }

  LaunchedEffect(state, capture.transitionAfterMs) {
    if (capture.enabled && capture.transitionAfterMs > 0L) {
      delay(capture.transitionAfterMs)
      if (!state.isActive) state.toggle()
    }
  }

  LaunchedEffect(state.isActive) {
    if (!state.isActive) {
      state.publish(EffectSnapshot())
      return@LaunchedEffect
    }

    if (capture.fixedEffect) {
      state.publish(EffectSnapshot(upsideDownAmount = 1f, timeSeconds = 4f, realRandom = .42f))
      return@LaunchedEffect
    }
    var startNanos = 0L
    while (true) {
      withFrameNanos { frameNanos ->
        if (startNanos == 0L) startNanos = frameNanos
        val elapsedMillis = (frameNanos - startNanos) / NanosPerMilli
        state.publish(snapshotAt(elapsedMillis))
      }
    }
  }

  return state
}

private fun snapshotAt(elapsedMillis: Long): EffectSnapshot {
  return EffectSnapshot(
      glitchIntensity = glitchIntensityAt(elapsedMillis),
      upsideDownAmount = if (elapsedMillis >= UpsideDownStartMillis) 1f else 0f,
      timeSeconds = elapsedMillis / MillisPerSecond,
      realRandom = Random.nextFloat(),
  )
}

private fun glitchIntensityAt(elapsedMillis: Long): Float {
  return when {
    elapsedMillis < FirstBurstMillis ->
      1f - elapsedMillis / FirstBurstMillis.toFloat()

    elapsedMillis < SecondBurstStartMillis -> 0f

    elapsedMillis < SecondBurstStartMillis + SecondBurstMillis ->
      1f - (elapsedMillis - SecondBurstStartMillis) / SecondBurstMillis.toFloat()

    else -> 0f
  }
}

private const val FirstBurstMillis = 200L
private const val SecondBurstStartMillis = 500L
private const val SecondBurstMillis = 500L
private const val UpsideDownStartMillis = 1100L
private const val NanosPerMilli = 1_000_000L
private const val MillisPerSecond = 1000f
