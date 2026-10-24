package dev.maxkach.shaderworkshop

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Before

class LauncherTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Before fun freezeAnimationForNavigationAssertions() {
        compose.activity.intent.putExtra("fixed", true)
        compose.activityRule.scenario.recreate()
    }

    @Test fun fourEntriesOpenTheirStateAndReturnToLauncher() {
        for ((label, active) in listOf(
            "Spotlight: Start" to false, "Spotlight: Finish" to true,
            "Upside Down: Start" to false, "Upside Down: Finish" to true,
        )) {
            compose.onAllNodes(hasClickAction()).assertCountEquals(4)
            compose.onNodeWithContentDescription(label).performClick()
            compose.onNodeWithText("Cappuccino").assertIsDisplayed()
            compose.onNodeWithContentDescription(if (active) "Back to normal" else "Upside Down").assertIsDisplayed()
            compose.onNodeWithContentDescription("Back").performClick()
            compose.onNodeWithContentDescription(label).assertIsDisplayed()
        }
    }

    @Test fun systemBackReturnsAndReentryResetsTheExercise() {
        compose.onNodeWithContentDescription("Spotlight: Start").performClick()
        compose.onNodeWithContentDescription("Upside Down").performClick()
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.onNodeWithContentDescription("Spotlight: Start").performClick()
        compose.onNodeWithContentDescription("Upside Down").assertIsDisplayed()
    }

    @Test fun recreationKeepsTheSelectedExercise() {
        compose.onNodeWithContentDescription("Upside Down: Finish").performClick()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("Cappuccino").assertIsDisplayed()
        compose.onNodeWithContentDescription("Back to normal").assertIsDisplayed()
    }
}
