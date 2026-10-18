package dev.maxkach.shaderworkshop.examples

import androidx.compose.runtime.Composable
import dev.maxkach.shaderworkshop.exercise2.end.Screen

@Composable
fun NoVideoScreen(startActive: Boolean = false, onBack: () -> Unit = {}) {
    Screen(startActive, onBack)
}
