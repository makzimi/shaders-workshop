package dev.maxkach.shaderworkshop.exercise2.start

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.start.glitch.upsideDownGlitch
import dev.maxkach.shaderworkshop.exercise2.start.grading.upsideDownGrading
import dev.maxkach.shaderworkshop.exercise2.start.snow.upsideDownSnow
import dev.maxkach.shaderworkshop.exercise2.start.vines.upsideDownVines

@Composable
fun Modifier.workshopEffect(effect: UpsideDownEffectState): Modifier {
    var result = this

    return result
        .upsideDownSnow(effect)
        .upsideDownVines(effect)
        .upsideDownGlitch(effect)
        .upsideDownGrading(effect)

}
