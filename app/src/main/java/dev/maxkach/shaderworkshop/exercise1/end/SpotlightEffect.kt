package dev.maxkach.shaderworkshop.exercise1.end

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.kotlinEffect
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState

@Composable
fun Modifier.spotlightWorkshopEffect(effect: UpsideDownEffectState): Modifier {
    val spotlight = remember { kotlinSpotlight() }
    return if (effect.isActive) kotlinEffect(spotlight, effect.snapshot.timeSeconds) else this
}
