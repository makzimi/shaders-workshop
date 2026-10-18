package dev.maxkach.shaderworkshop.exercise2.end

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.shared.ProductScreen

@Composable
fun Screen(startActive: Boolean = true, onBack: () -> Unit = {}) {
    ProductScreen(startActive, onBack) { effect -> Modifier.workshopEffect(effect) }
}
