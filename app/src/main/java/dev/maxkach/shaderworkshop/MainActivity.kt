package dev.maxkach.shaderworkshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dev.maxkach.shaderworkshop.examples.*
import dev.maxkach.shaderworkshop.exercise1.start.Screen as SpotlightStart
import dev.maxkach.shaderworkshop.exercise1.end.Screen as SpotlightFinish
import dev.maxkach.shaderworkshop.exercise2.start.Screen as UpsideDownStart
import dev.maxkach.shaderworkshop.exercise2.end.Screen as UpsideDownFinish
import dev.maxkach.shaderworkshop.teaching.CaptureOptions
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions

private val exampleToRun: WorkshopExample? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val example = WorkshopExample.entries.firstOrNull {
            it.name == intent.getStringExtra("example")
        } ?: exampleToRun
        val capture = CaptureOptions(
            enabled = intent.getBooleanExtra("capture", false),
            paused = intent.getBooleanExtra("paused", false),
            fixedEffect = intent.getBooleanExtra("fixed", false),
            videoPositionMs = intent.getLongExtra("positionMs", 2000L),
            transitionAfterMs = intent.getLongExtra("transitionAfterMs", 0L).coerceIn(0, 60_000),
        )
        if (capture.enabled) WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
        val active = intent.getBooleanExtra("effect", example == WorkshopExample.SpotlightFinish || example == WorkshopExample.UpsideDownFinish)
        setContent {
            CompositionLocalProvider(LocalCaptureOptions provides capture) {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    var selectedName by rememberSaveable { mutableStateOf(example?.name) }
                    var startActive by rememberSaveable { mutableStateOf(active) }
                    val selected = selectedName?.let(WorkshopExample::valueOf)
                    val back = { selectedName = null }
                    BackHandler(enabled = selected != null, onBack = back)
                    key(selected) {
                        when (selected) {
                            null -> ExerciseLauncher { choice ->
                                startActive = choice == WorkshopExample.SpotlightFinish || choice == WorkshopExample.UpsideDownFinish
                                selectedName = choice.name
                            }
                            WorkshopExample.SpotlightStart, WorkshopExample.Starter -> SpotlightStart(startActive, back)
                            WorkshopExample.SpotlightFinish -> SpotlightFinish(startActive, back)
                            WorkshopExample.FirstShader -> FirstShaderScreen(startActive, onBack = back)
                            WorkshopExample.UpsideDownStart -> UpsideDownStart(startActive, back)
                            WorkshopExample.UpsideDownFinish, WorkshopExample.NoVideoAgsl -> UpsideDownFinish(startActive, back)
                            WorkshopExample.FirstShaderKotlin -> FirstShaderScreen(startActive, kotlin = true, onBack = back)
                            WorkshopExample.SurfaceViewAgsl -> SurfaceViewScreen(startActive, back)
                            WorkshopExample.TextureViewAgsl -> TextureViewScreen(startActive, back)
                            WorkshopExample.SurfaceViewPipeline -> Media3Screen(startActive, shadeUi = intent.getBooleanExtra("ui", true), onBack = back)
                            WorkshopExample.SurfaceViewRawGl -> GlRendererScreen(startActive, continuous = intent.getBooleanExtra("continuous", true), onBack = back)
                        }
                    }
                }
            }
        }
    }
}
