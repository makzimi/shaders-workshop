package dev.maxkach.shaderworkshop.teaching

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

data class CaptureOptions(
    val enabled: Boolean = false,
    val paused: Boolean = false,
    val fixedEffect: Boolean = false,
    val videoPositionMs: Long = 2000L,
    val transitionAfterMs: Long = 0L,
    val sourceBuild: Boolean = false,
)
val LocalCaptureOptions = staticCompositionLocalOf { CaptureOptions() }

internal fun captureTransitionDelayMs(enabled: Boolean, requested: Long): Long =
    if (enabled) requested.coerceIn(0L, 60_000L) else 0L

internal fun captureLayoutIgnoresSystemBars(captureEnabled: Boolean): Boolean = captureEnabled

@Composable
fun Modifier.captureAwareStatusBarsPadding(): Modifier =
    if (captureLayoutIgnoresSystemBars(LocalCaptureOptions.current.enabled)) this else statusBarsPadding()

@Composable
fun Modifier.captureAwareNavigationBarsPadding(): Modifier =
    if (captureLayoutIgnoresSystemBars(LocalCaptureOptions.current.enabled)) this else navigationBarsPadding()
