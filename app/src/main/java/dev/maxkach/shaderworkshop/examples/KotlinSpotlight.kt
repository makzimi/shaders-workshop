package dev.maxkach.shaderworkshop.examples

import dev.maxkach.shaderworkshop.KotlinExample
import ru.redbyte.redbytefx.*

fun kotlinSpotlight(): KotlinExample {
    var timeHandle: FxParam.Float? = null
    val effect = redbytefx {
        val t = uniformTime(0f, "time")
        timeHandle = t
        val uv = fragCoord / resolution
        val center = float2(0.5f + 0.25f * sin(t), 0.5f)
        val delta = (uv - center) * float2(resolution.x / resolution.y, 1f)
        val light = 1f - smoothstep(0.08f, 0.35f, length(delta))
        val c = let(sample(), "input_color")
        val gain = 0.25f + 0.75f * light
        color(c.r * gain, c.g * gain, c.b * gain, c.a)
    }
    return KotlinExample(effect, requireNotNull(timeHandle))
}
