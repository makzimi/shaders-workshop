package dev.maxkach.shaderworkshop.exercise1.start

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.shared.ProductScreen

@Composable
fun Screen(
    startActive: Boolean = false,
    onBack: () -> Unit = {}
) {
    ProductScreen(startActive, onBack) { effect ->
        Modifier.spotlightWorkshopEffect(effect)
    }
}
