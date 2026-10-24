package dev.maxkach.shaderworkshop.exercise2.end

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.end.glitch.upsideDownGlitch
import dev.maxkach.shaderworkshop.exercise2.end.grading.upsideDownGrading
import dev.maxkach.shaderworkshop.exercise2.end.snow.upsideDownSnow
import dev.maxkach.shaderworkshop.exercise2.end.vines.upsideDownVines

@Composable
fun Modifier.workshopEffect(effect: UpsideDownEffectState): Modifier {
    return this.upsideDownSnow(effect)
        .upsideDownVines(effect)
        .upsideDownGlitch(effect)
        .upsideDownGrading(effect)
}
