package dev.maxkach.shaderworkshop.exercise2.start

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.start.agsl.upsideDownAgsl
import dev.maxkach.shaderworkshop.shared.effect.mesh.upsideDownRouteVines
import dev.maxkach.shaderworkshop.shared.effect.snow.upsideDownSnow

@Composable
fun Modifier.workshopEffect(effect: UpsideDownEffectState): Modifier {
    var result = this
    // STEP-BEGIN upside-attach-1: Add the prepared original three-layer snow overlay.
    /*
    result = result.upsideDownSnow(effect)
    */
    // STEP-BEGIN upside-attach-2: Add the prepared original mesh/Canvas vines.
    /*
    result = result.upsideDownRouteVines(effect)
    */
    // STEP-BEGIN upside-attach-3: Attach our grade/glitch shaders. Vines already have their own layer.
    /*
    result = result.upsideDownAgsl(effect, drawVines = false)
    */
    return result
}
