package dev.maxkach.shaderworkshop.player

import android.content.Context
import android.opengl.GLSurfaceView
import dev.maxkach.shaderworkshop.shared.effect.gl.UpsideDownVideoRenderer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal class OwnedGlSurfaceView(
    context: Context,
    private val renderer: UpsideDownVideoRenderer,
    private val beforeShutdown: () -> Unit,
) : GLSurfaceView(context) {
    private var closed = false

    fun shutdown() {
        if (closed) return
        closed = true

        beforeShutdown()
        renderer.stopRendering()
        val released = CountDownLatch(1)
        queueEvent {
            try { renderer.release() } finally { released.countDown() }
        }

        if (!released.await(2, TimeUnit.SECONDS)) renderer.releaseSurfaceTexture()
    }

    override fun onDetachedFromWindow() {
        shutdown()
        super.onDetachedFromWindow()
    }
}
