package dev.maxkach.shaderworkshop.player

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun rememberVideoSize(player: ExoPlayer): IntSize {
  var reported by remember(player) {
    mutableStateOf(IntSize(player.videoSize.width, player.videoSize.height))
  }

  DisposableEffect(player) {
    val listener = object : Player.Listener {
      override fun onVideoSizeChanged(videoSize: VideoSize) {
        Log.d(VideoAspectTag, "onVideoSizeChanged: ${videoSize.width}x${videoSize.height}")
        reported = IntSize(videoSize.width, videoSize.height)
      }
    }
    player.addListener(listener)
    onDispose { player.removeListener(listener) }
  }

  return if (reported.width > 0 && reported.height > 0) reported else BundledClipSize
}

private val BundledClipSize = IntSize(1086, 1852)

private const val VideoAspectTag = "VideoAspect"

fun Modifier.centerCropVideo(videoSize: IntSize): Modifier = layout { measurable, constraints ->
  val containerWidth = constraints.maxWidth
  val containerHeight = constraints.maxHeight

  val known = minOf(videoSize.width, videoSize.height, containerWidth, containerHeight) > 0
  if (!known) {
    val placeable = measurable.measure(constraints)
    return@layout layout(placeable.width, placeable.height) { placeable.place(0, 0) }
  }

  val scale = max(
      containerWidth.toFloat() / videoSize.width,
      containerHeight.toFloat() / videoSize.height,
  )
  val childWidth = (videoSize.width * scale).roundToInt()
  val childHeight = (videoSize.height * scale).roundToInt()

  val placeable = measurable.measure(Constraints.fixed(childWidth, childHeight))
  layout(containerWidth, containerHeight) {
    placeable.place((containerWidth - childWidth) / 2, (containerHeight - childHeight) / 2)
  }
}
