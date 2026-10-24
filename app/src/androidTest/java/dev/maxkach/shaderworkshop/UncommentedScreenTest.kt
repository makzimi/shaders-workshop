package dev.maxkach.shaderworkshop

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import dev.maxkach.shaderworkshop.exercise1.end.Screen as SpotlightFinish
import dev.maxkach.shaderworkshop.examples.NoVideoScreen
import dev.maxkach.shaderworkshop.exercise1.start.Screen as SpotlightStart
import dev.maxkach.shaderworkshop.exercise2.start.Screen as UpsideDownStart
import dev.maxkach.shaderworkshop.teaching.CaptureOptions
import dev.maxkach.shaderworkshop.teaching.LocalCaptureOptions
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test

/** Run only in the disposable project whose actual source comments have been removed. */
class UncommentedScreenTest {
    @get:Rule val compose = createComposeRule()

    private fun compare(spotlight: Boolean) {
        assumeTrue(InstrumentationRegistry.getArguments().getString("uncommented") == "true")
        val starter = mutableStateOf(true)
        compose.setContent {
            CompositionLocalProvider(LocalCaptureOptions provides CaptureOptions(enabled = true, fixedEffect = true)) {
                MaterialTheme {
                    key(starter.value) {
                        if (starter.value) if (spotlight) SpotlightStart(startActive = true) else UpsideDownStart(startActive = true)
                        else if (spotlight) SpotlightFinish(startActive = true)
                        else NoVideoScreen(startActive = true)
                    }
                }
            }
        }
        compose.onNodeWithText("Cappuccino").assertIsDisplayed()
        val actual = compose.onRoot().captureToImage().toPixelMap()
        compose.onNodeWithContentDescription("Back to normal").performClick()
        compose.onNodeWithContentDescription("Upside Down").assertIsDisplayed()
        compose.onNodeWithContentDescription("Upside Down").performClick()
        compose.onNodeWithContentDescription("Back to normal").assertIsDisplayed()
        compose.runOnIdle { starter.value = false }
        val expected = compose.onRoot().captureToImage().toPixelMap()
        var difference = 0f
        var count = 0
        for (y in 8 until actual.height - 8 step 3) for (x in 8 until actual.width - 8 step 3) {
            val a = actual[x,y]; val e = expected[x,y]
            difference += kotlin.math.abs(a.red - e.red) + kotlin.math.abs(a.green - e.green) + kotlin.math.abs(a.blue - e.blue)
            count += 3
        }
        assertTrue("Actual uncommented Start screen must match Finish: ${difference / count}", difference / count < 0.002f)
    }

    @Test fun completedSpotlightStartMatchesFinish() = compare(spotlight = true)
    @Test fun completedUpsideDownStartMatchesFinish() = compare(spotlight = false)
}
