package dev.maxkach.shaderworkshop.exercise1.start

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.kotlinEffect
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState

@Composable
fun Modifier.spotlightWorkshopEffect(effect: UpsideDownEffectState): Modifier {
    val spotlight = remember { kotlinSpotlight() }
    var result = this
    // STEP-BEGIN spotlight-attach-1: Attach only while the original product effect icon is active.
    /*
    result = if (effect.isActive) result.kotlinEffect(spotlight, effect.snapshot.timeSeconds) else result
    */
    return result
}
