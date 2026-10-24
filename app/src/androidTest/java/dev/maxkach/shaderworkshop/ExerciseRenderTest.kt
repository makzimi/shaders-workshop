package dev.maxkach.shaderworkshop

import android.graphics.BlendMode
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
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
import org.junit.Assume.assumeTrue
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
        val difference = difference(actual, expected)
        assertTrue("Uncommented exercise must render like the independent original answer: $difference", difference < 0.002f)
    }

    private fun difference(actual: PixelMap, expected: PixelMap): Float {
        var difference = 0f
        var count = 0
        for (y in 8 until actual.height - 8 step 3) for (x in 8 until actual.width - 8 step 3) {
            val a = actual[x, y]; val e = expected[x, y]
            difference += kotlin.math.abs(a.red - e.red) + kotlin.math.abs(a.green - e.green) + kotlin.math.abs(a.blue - e.blue)
            count += 3
        }
        return difference / count
    }

    @Test fun uncommentedGradeRendersLikeOriginal() {
        compare(uncomment(dev.maxkach.shaderworkshop.exercise2.start.grading.GRADING_SHADER), dev.maxkach.shaderworkshop.exercise2.end.grading.GRADING_SHADER, "image") { shader, _, _ ->
            shader.setFloatUniform("darknessIntensity", 0.4f)
        }
    }

    @Test fun uncommentedGlitchRendersLikeOriginal() {
        compare(CommentedLines.uncommentAll(dev.maxkach.shaderworkshop.exercise2.start.glitch.GLITCH_SHADER), dev.maxkach.shaderworkshop.exercise2.end.glitch.GLITCH_SHADER, "image") { shader, w, h ->
            shader.setFloatUniform("imageSize", w, h)
            shader.setIntUniform("slices", 70)
            shader.setFloatUniform("intensity", 0.8f)
            shader.setFloatUniform("realRandom", 0.42f)
        }
    }

    @Test fun uncommentedVinesRenderLikeOriginal() {
        assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        val starter = dev.maxkach.shaderworkshop.exercise2.start.vines.VINES_VERTEX_SHADER to
            dev.maxkach.shaderworkshop.exercise2.start.vines.VINES_FRAGMENT_SHADER
        val completed = uncomment(starter.first) to starter.second
        val reference = dev.maxkach.shaderworkshop.exercise2.end.vines.VINES_VERTEX_SHADER to
            dev.maxkach.shaderworkshop.exercise2.end.vines.VINES_FRAGMENT_SHADER
        val shaders = mutableStateOf(starter)
        val paint = Paint()
        compose.setContent {
            Canvas(Modifier.fillMaxSize()) {
                drawRect(Color.White)
                val vines = VinesTestMeshes(shaders.value.first, shaders.value.second, size.width, size.height, progress = 0.6f)
                drawIntoCanvas { canvas ->
                    vines.meshes.forEach { canvas.nativeCanvas.drawMesh(it, BlendMode.SRC_OVER, paint) }
                }
            }
        }
        assertTrue("The commented Start must draw no vines", vinePixels(compose.onRoot().captureToImage().toPixelMap()) == 0)
        compose.runOnIdle { shaders.value = completed }
        val actual = compose.onRoot().captureToImage().toPixelMap()
        compose.runOnIdle { shaders.value = reference }
        val expected = compose.onRoot().captureToImage().toPixelMap()
        assertTrue("The original answer must actually draw vines", vinePixels(expected) > 100)
        val difference = difference(actual, expected)
        assertTrue("Uncommented vines must render like the independent original answer: $difference", difference < 0.002f)
    }

    private fun vinePixels(image: PixelMap): Int {
        var count = 0
        for (y in 0 until image.height step 3) for (x in 0 until image.width step 3) {
            if (image[x, y].red < 0.5f) count++
        }
        return count
    }

    @Test fun uncommentedSnowRendersLikeOriginal() {
        val starter = dev.maxkach.shaderworkshop.exercise2.start.snow.SNOW_SHADER
        val completed = CommentedLines.uncommentAll(starter)
        val reference = dev.maxkach.shaderworkshop.exercise2.end.snow.SNOW_SHADER
        val source = mutableStateOf(starter)
        compose.setContent {
            val shader = remember(source.value) { RuntimeShader(source.value) }
            Canvas(Modifier.fillMaxSize()) {
                drawRect(Color.Black)
                shader.setFloatUniform("size", size.width, size.height)
                shader.setFloatUniform("time", 4f)
                drawRect(ShaderBrush(shader))
            }
        }
        assertTrue("The commented Start must draw no snow", snowPixels(compose.onRoot().captureToImage().toPixelMap()) == 0)
        compose.runOnIdle { source.value = completed }
        val actual = compose.onRoot().captureToImage().toPixelMap()
        compose.runOnIdle { source.value = reference }
        val expected = compose.onRoot().captureToImage().toPixelMap()
        assertTrue("The original answer must actually draw snow", snowPixels(expected) > 100)
        val difference = difference(actual, expected)
        assertTrue("Uncommented snow must render like the independent original answer: $difference", difference < 0.002f)
    }

    private fun snowPixels(image: PixelMap): Int {
        var count = 0
        for (y in 0 until image.height step 3) for (x in 0 until image.width step 3) {
            if (image[x, y].red > 0.1f) count++
        }
        return count
    }
}
