package dev.maxkach.shaderworkshop.player
import androidx.compose.runtime.staticCompositionLocalOf
import dev.maxkach.shaderworkshop.R

data class DemoClip(val resource: Int)
val LocalDemoClip = staticCompositionLocalOf { DemoClip(R.raw.cappuccino) }
