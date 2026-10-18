package dev.maxkach.shaderworkshop.player

import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.Looper
import android.view.Surface
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState
import dev.maxkach.shaderworkshop.shared.effect.gl.UpsideDownVideoRenderer

@Composable
fun GlVideoSurface(
  player: ExoPlayer,
  state: UpsideDownEffectState,
  modifier: Modifier = Modifier,
  renderContinuously: Boolean = true,
) {
  val videoSize = rememberVideoSize(player)
  val lifecycle = LocalLifecycleOwner.current.lifecycle
  val surfaces = remember(player, state) { ResourceSlot<Surface> { it.release() } }
  var glView by remember { mutableStateOf<OwnedGlSurfaceView?>(null) }

  val renderer = remember(state) {
    UpsideDownVideoRenderer(
        state = state,
        onSurfaceTextureReady = { surfaceTexture ->

          Handler(Looper.getMainLooper()).post {
            if (!surfaces.isClosed && !surfaceTexture.isReleased) {
              val surface = Surface(surfaceTexture)
              if (surfaces.replace(surface)) player.setVideoSurface(surface)
            }
          }
        },
    )
  }

  DisposableEffect(player, renderer) {
    renderer.videoWidth = player.videoSize.width
    renderer.videoHeight = player.videoSize.height
    onDispose {
      player.setVideoSurface(null)
      surfaces.close()
      glView?.shutdown()
    }
  }

  DisposableEffect(lifecycle, glView) {
    val view = glView
    val observer = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_RESUME -> view?.onResume()
        Lifecycle.Event.ON_PAUSE -> view?.onPause()
        else -> Unit
      }
    }
    lifecycle.addObserver(observer)
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) view?.onResume()
    onDispose { lifecycle.removeObserver(observer) }
  }

  renderer.videoWidth = videoSize.width
  renderer.videoHeight = videoSize.height

  Box(modifier = modifier.clipToBounds()) {
    AndroidView(
        modifier = Modifier.centerCropVideo(videoSize),
        factory = { context ->
          OwnedGlSurfaceView(context, renderer) {
            player.setVideoSurface(null)
            surfaces.close()
          }.apply {
            setEGLContextClientVersion(2)
            preserveEGLContextOnPause = true
            glView = this

            renderer.onFrameAvailable = ::requestRender
            setRenderer(renderer)
          }
        },
        update = { view ->
          view.renderMode = if (renderContinuously) {
            GLSurfaceView.RENDERMODE_CONTINUOUSLY
          } else {
            GLSurfaceView.RENDERMODE_WHEN_DIRTY
          }
        },
        onReset = null,
        onRelease = { view ->
          view.shutdown()
        },
    )
  }
}
