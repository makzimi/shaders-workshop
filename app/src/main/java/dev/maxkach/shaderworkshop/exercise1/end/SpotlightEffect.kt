package dev.maxkach.shaderworkshop.exercise1.end

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.agslEffect
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState

@Composable
fun Modifier.spotlightWorkshopEffect(effect: UpsideDownEffectState): Modifier {
    return if (effect.isActive) agslEffect(spotlightSource, effect.snapshot.timeSeconds) else this
}
