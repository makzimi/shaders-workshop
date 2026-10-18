package dev.maxkach.shaderworkshop.examples

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.exercise1.end.spotlightWorkshopEffect
import dev.maxkach.shaderworkshop.kotlinEffect
import dev.maxkach.shaderworkshop.shared.ProductScreen

@Composable
fun FirstShaderScreen(startActive: Boolean = false, kotlin: Boolean = false, onBack: () -> Unit = {}) {
    val typed = remember { kotlinSpotlight() }
    ProductScreen(startActive, onBack) { effect ->
        if (kotlin && effect.isActive) Modifier.kotlinEffect(typed, effect.snapshot.timeSeconds)
        else Modifier.spotlightWorkshopEffect(effect)
    }
}
