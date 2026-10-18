package dev.maxkach.shaderworkshop

import android.graphics.RuntimeShader
import org.junit.Assert.assertTrue
import org.junit.Test

/** Removing a step's comment delimiters must compile at every pause in the live workshop. */
class CommentedExerciseTest {
    private fun checkSteps(source: String, minimum: Int) {
        val step = Regex("(?m)(// STEP-BEGIN [^\\n]+\\n)\\s*/\\*\\n([\\s\\S]*?)\\n\\s*\\*/")
        assertTrue("The whole answer must be present as small numbered commented steps", step.findAll(source).count() >= minimum)
        var current = source
        RuntimeShader(current)
        while (step.containsMatchIn(current)) {
            current = step.replaceFirst(current, "$1$2")
            RuntimeShader(current)
        }
    }

    @Test fun spotlightCanBeUncommentedOneStepAtATime() {
        checkSteps(dev.maxkach.shaderworkshop.exercise1.start.spotlightSource, 4)
    }

    @Test fun originalGradeCanBeUncommentedOneStepAtATime() {
        checkSteps(dev.maxkach.shaderworkshop.exercise2.start.agsl.upsideDownShader(0), 8)
        checkSteps(dev.maxkach.shaderworkshop.exercise2.start.agsl.upsideDownShader(4), 8)
    }

    @Test fun originalGlitchCanBeUncommentedOneStepAtATime() {
        checkSteps(dev.maxkach.shaderworkshop.exercise2.start.agsl.GLITCH_SHADER, 4)
    }
}
