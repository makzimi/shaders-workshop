package dev.maxkach.shaderworkshop.exercise1.start

import dev.maxkach.shaderworkshop.KotlinExample
import ru.redbyte.redbytefx.*

fun kotlinSpotlight(): KotlinExample {
    var timeHandle: FxParam.Float? = null
    val effect = redbytefx {
        val t = uniformTime(0f, "time")
        timeHandle = t
        var c = let(sample(), "input_color")
        // STEP-BEGIN spotlight-1: Normalize pixel coordinates.
        /*
        val uv = fragCoord / resolution
        */
        // STEP-BEGIN spotlight-2: Move the center with time. Try 0.25f for the horizontal travel.
        /*
        val center = float2(0.5f + 0.25f * sin(t), 0.5f)
        */
        // STEP-BEGIN spotlight-3: Correct the aspect ratio before measuring distance.
        /*
        val delta = (uv - center) * float2(resolution.x / resolution.y, 1f)
        */
        // STEP-BEGIN spotlight-4: Make a soft edge. Try changing the 0.08f core or 0.35f outer radius.
        /*
        val light = 1f - smoothstep(0.08f, 0.35f, length(delta))
        */
        // STEP-BEGIN spotlight-5: Dim RGB while preserving sampled alpha. Try changing the 0.25f brightness floor.
        /*
        val gain = 0.25f + 0.75f * light
        c = color(c.r * gain, c.g * gain, c.b * gain, c.a)
        */
        c
    }
    return KotlinExample(effect, requireNotNull(timeHandle))
}
