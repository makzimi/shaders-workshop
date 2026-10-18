package dev.maxkach.shaderworkshop.player

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.view.SurfaceView
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Effect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import dev.maxkach.shaderworkshop.R
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions

@OptIn(UnstableApi::class)
@Composable
fun rememberDemoPlayer(effects: ImmutableList<Effect> = persistentListOf()): ExoPlayer {
  val context = LocalContext.current
  val capture = LocalCaptureOptions.current
  val clip = LocalDemoClip.current

  val player = remember {
    ExoPlayer.Builder(context).build().apply {
      setMediaItem(MediaItem.fromUri(rawResourceUri(context.packageName, clip.resource)))
      repeatMode = Player.REPEAT_MODE_ALL
      volume = 0f
      if (effects.isNotEmpty()) {
        setVideoEffects(effects)
      }
      if (capture.enabled) seekTo(capture.videoPositionMs)
      playWhenReady = !capture.paused
      prepare()
    }
  }

  val lifecycle = LocalLifecycleOwner.current.lifecycle
  DisposableEffect(player, lifecycle) {
    var resumePlayback = !capture.paused
    val observer = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_STOP -> {
          resumePlayback = player.playWhenReady
          player.pause()
        }
        Lifecycle.Event.ON_START -> player.playWhenReady = resumePlayback
        else -> Unit
      }
    }
    lifecycle.addObserver(observer)
    onDispose {
      lifecycle.removeObserver(observer)
      player.release()
    }
  }

  return player
}

private fun rawResourceUri(packageName: String, resourceId: Int): String =
  "android.resource://$packageName/$resourceId"

@Composable
fun SurfaceViewVideo(
  player: ExoPlayer,
  modifier: Modifier = Modifier,
) {
  val videoSize = rememberVideoSize(player)

  Box(modifier = modifier.clipToBounds()) {
    AndroidView(
        modifier = Modifier.centerCropVideo(videoSize),
        factory = { context -> SurfaceView(context) },
        update = { view -> player.setVideoSurfaceView(view) },
    )
  }
}

@Composable
fun TextureViewVideo(
  player: ExoPlayer,
  modifier: Modifier = Modifier,
) {
  val videoSize = rememberVideoSize(player)

  Box(modifier = modifier.clipToBounds()) {
    AndroidView(
        modifier = Modifier.centerCropVideo(videoSize),
        factory = { context -> TextureView(context) },
        update = { view -> player.setVideoTextureView(view) },
    )
  }
}
