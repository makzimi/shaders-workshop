package dev.maxkach.shaderworkshop

import android.graphics.RuntimeShader
import android.os.Build
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test

/** Removing a step's comment delimiters must compile at every pause in the live workshop. */
class CommentedExerciseTest {
    private fun checkSteps(source: String, minimum: Int, compile: (String) -> Unit = { RuntimeShader(it) }) {
        val step = Regex("(?m)(// STEP-BEGIN [^\\n]+\\n)\\s*/\\*\\n([\\s\\S]*?)\\n\\s*\\*/")
        assertTrue("The whole answer must be present as small numbered commented steps", step.findAll(source).count() >= minimum)
        var current = source
        compile(current)
        while (step.containsMatchIn(current)) {
            current = step.replaceFirst(current, "$1$2")
            compile(current)
        }
    }

    @Test fun originalGradeCanBeUncommentedOneStepAtATime() {
        checkSteps(dev.maxkach.shaderworkshop.exercise2.start.grading.GRADING_SHADER, 10)
    }

    @Test fun originalGlitchCanBeUncommentedOneStepAtATime() {
        checkSteps(dev.maxkach.shaderworkshop.exercise2.start.glitch.GLITCH_SHADER, 4)
    }

    @Test fun vinesCanBeUncommentedOneStepAtATime() {
        assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        val fragment = dev.maxkach.shaderworkshop.exercise2.start.vines.VINES_FRAGMENT_SHADER
        checkSteps(dev.maxkach.shaderworkshop.exercise2.start.vines.VINES_VERTEX_SHADER, 6) {
            VinesTestMeshes(it, fragment, 1080f, 2424f, progress = 0.6f)
        }
    }
}
