package dev.maxkach.shaderworkshop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dev.maxkach.shaderworkshop.card.LocalProductBack
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions

@Composable
fun SampleScaffold(player: ExoPlayer? = null, onBack: () -> Unit = {}, content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalProductBack provides onBack) { content() }
        if (player != null && !LocalCaptureOptions.current.enabled) {
            var playing by remember(player) { mutableStateOf(player.playWhenReady) }
            DisposableEffect(player) {
                val listener = object : Player.Listener {
                    override fun onPlayWhenReadyChanged(value: Boolean, reason: Int) { playing = value }
                }
                player.addListener(listener)
                onDispose { player.removeListener(listener) }
            }
            TextButton(
                onClick = { if (player.playWhenReady) player.pause() else player.play() },
                modifier = Modifier.align(Alignment.TopStart).statusBarsPadding()
                    .padding(start = 12.dp, top = 174.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            ) { Text(if (playing) "Pause" else "Play", color = Color.White, fontSize = 13.sp) }
        }
    }
}
