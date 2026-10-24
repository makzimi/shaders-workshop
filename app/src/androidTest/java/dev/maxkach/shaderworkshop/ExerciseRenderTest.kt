package dev.maxkach.shaderworkshop

import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import dev.maxkach.shaderworkshop.exercise1.start.Screen as SpotlightStart
import dev.maxkach.shaderworkshop.exercise2.start.Screen as UpsideDownStart
import dev.maxkach.shaderworkshop.teaching.CaptureOptions
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ExerciseRenderTest {
    @get:Rule val compose = createComposeRule()

    private fun uncomment(source: String): String = Regex(
        "(?m)(// STEP-BEGIN [^\\n]+\\n)\\s*/\\*\\n([\\s\\S]*?)\\n\\s*\\*/"
    ).replace(source, "$1$2")

    private fun compare(completed: String, reference: String, input: String, setup: (RuntimeShader, Float, Float) -> Unit) {
        val source = mutableStateOf(completed)
        compose.setContent {
            CompositionLocalProvider(LocalCaptureOptions provides CaptureOptions(enabled = true, fixedEffect = true)) {
                MaterialTheme {
                    val shader = remember(source.value) { RuntimeShader(source.value) }
                    Box(Modifier.fillMaxSize().graphicsLayer {
                        setup(shader, size.width, size.height)
                        renderEffect = android.graphics.RenderEffect.createRuntimeShaderEffect(shader, input).asComposeRenderEffect()
                    }) { SpotlightStart(startActive = true) }
                }
            }
        }
        val actual = compose.onRoot().captureToImage().toPixelMap()
        compose.runOnIdle { source.value = reference }
        val expected = compose.onRoot().captureToImage().toPixelMap()
        var difference = 0f
        var count = 0
        for (y in 8 until actual.height - 8 step 3) for (x in 8 until actual.width - 8 step 3) {
            val a = actual[x, y]; val e = expected[x, y]
            difference += kotlin.math.abs(a.red - e.red) + kotlin.math.abs(a.green - e.green) + kotlin.math.abs(a.blue - e.blue)
            count += 3
        }
        assertTrue("Uncommented exercise must render like the independent original answer: ${difference / count}", difference / count < 0.002f)
    }

    @Test fun uncommentedGradeRendersLikeOriginal() {
        compare(uncomment(dev.maxkach.shaderworkshop.exercise2.start.agsl.upsideDownShader(0)), dev.maxkach.shaderworkshop.exercise2.end.agsl.upsideDownShader(0), "image") { shader, w, h ->
            shader.setFloatUniform("imageSize", w, h)
            shader.setFloatUniform("time", 4f)
            shader.setFloatUniform("darknessIntensity", 0.4f)
        }
    }

    @Test fun uncommentedGlitchRendersLikeOriginal() {
        compare(uncomment(dev.maxkach.shaderworkshop.exercise2.start.agsl.GLITCH_SHADER), dev.maxkach.shaderworkshop.exercise2.end.agsl.GLITCH_SHADER, "image") { shader, w, h ->
            shader.setFloatUniform("imageSize", w, h)
            shader.setIntUniform("slices", 70)
            shader.setFloatUniform("intensity", 0.8f)
            shader.setFloatUniform("realRandom", 0.42f)
        }
    }
}
