package dev.maxkach.shaderworkshop.exercise1.start

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.agslEffect
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState

@Composable
fun Modifier.spotlightWorkshopEffect(effect: UpsideDownEffectState): Modifier {
    var result = this
    // STEP-BEGIN spotlight-attach-1: Attach only while the original product effect icon is active.
    /*
    result = if (effect.isActive) result.agslEffect(spotlightSource, effect.snapshot.timeSeconds) else result
    */
    return result
}
