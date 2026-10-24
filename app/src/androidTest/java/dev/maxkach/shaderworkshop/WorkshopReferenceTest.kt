package dev.maxkach.shaderworkshop

import android.graphics.RuntimeShader
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.maxkach.shaderworkshop.examples.FirstShaderScreen
import dev.maxkach.shaderworkshop.exercise1.start.Screen as SpotlightStart
import dev.maxkach.shaderworkshop.exercise1.end.Screen as SpotlightFinish
import dev.maxkach.shaderworkshop.exercise2.start.Screen as UpsideDownStart
import dev.maxkach.shaderworkshop.teaching.CaptureOptions
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WorkshopReferenceTest {
    @get:Rule val compose = createComposeRule()

    @Test fun starterIsTheProductWithoutWorkshopControls() {
        compose.setContent {
            CompositionLocalProvider(LocalCaptureOptions provides CaptureOptions(enabled = true, fixedEffect = true)) {
                MaterialTheme { SpotlightStart() }
            }
        }
        compose.onNodeWithText("Cappuccino").assertIsDisplayed()
        compose.onNodeWithContentDescription("Upside Down").assertIsDisplayed()
        for (label in listOf("Starter", "Finished", "SHADER WORKSHOP", "DevFest Baku", "Next", "Previous")) {
            compose.onNodeWithText(label).assertDoesNotExist()
        }
        compose.onNodeWithContentDescription("Upside Down").performClick()
        compose.onNodeWithContentDescription("Back to normal").assertIsDisplayed()
    }

    @Test fun rawAndKotlinFirstExerciseMatchOnReferenceCard() {
        val typed = mutableStateOf(false)
        compose.setContent {
            CompositionLocalProvider(LocalCaptureOptions provides CaptureOptions(enabled = true, fixedEffect = true)) {
                MaterialTheme {
                    if (typed.value) SpotlightFinish()
                    else FirstShaderScreen(startActive = true)
                }
            }
        }
        val raw = compose.onRoot().captureToImage().toPixelMap()
        compose.runOnIdle { typed.value = true }
        val kotlinPixels = compose.onRoot().captureToImage().toPixelMap()
        var difference = 0f
        var count = 0
        for (y in 10 until raw.height - 10 step 7) for (x in 10 until raw.width - 10 step 7) {
            difference += kotlin.math.abs(raw[x,y].red - kotlinPixels[x,y].red)
            count++
        }
        assertTrue("Same math must render the same card", difference / count < 0.025f)
    }

    @Test fun originalAndStarterShadersCompile() {
        listOf(
            dev.maxkach.shaderworkshop.exercise2.end.agsl.GLITCH_SHADER,
            dev.maxkach.shaderworkshop.exercise2.end.agsl.upsideDownShader(0),
            dev.maxkach.shaderworkshop.shared.effect.snow.SNOW_SHADER,
            dev.maxkach.shaderworkshop.exercise2.start.agsl.GLITCH_SHADER,
            dev.maxkach.shaderworkshop.exercise2.start.agsl.upsideDownShader(0),
            dev.maxkach.shaderworkshop.examples.spotlightSource,
        ).forEach { RuntimeShader(it) }
        dev.maxkach.shaderworkshop.exercise1.start.kotlinSpotlight().effect.newInstance()
        dev.maxkach.shaderworkshop.exercise1.end.kotlinSpotlight().effect.newInstance()
    }
}
