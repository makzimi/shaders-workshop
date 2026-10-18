package dev.maxkach.shaderworkshop.exercise2.end

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.agslEffect
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.end.agsl.upsideDownAgsl
import dev.maxkach.shaderworkshop.shared.effect.mesh.upsideDownRouteVines
import dev.maxkach.shaderworkshop.shared.effect.snow.upsideDownSnow

@Composable
fun Modifier.workshopEffect(effect: UpsideDownEffectState): Modifier {
    return this.upsideDownSnow(effect)
        .upsideDownRouteVines(effect)
        .upsideDownAgsl(effect, drawVines = false)
}
