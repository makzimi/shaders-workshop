package dev.maxkach.shaderworkshop.exercise1.end

import org.intellij.lang.annotations.Language

@Language("AGSL")
const val spotlightSource = """
uniform shader content;
uniform float2 resolution;
uniform float time;
half4 main(float2 p) {
    float2 uv = p / resolution;
    float2 center = float2(0.5 + 0.25 * sin(time), 0.5);
    float2 delta = (uv - center) * float2(resolution.x / resolution.y, 1.0);
    float light = 1.0 - smoothstep(0.08, 0.35, length(delta));
    half4 c = content.eval(p);
    return half4(c.rgb * (0.25 + 0.75 * light), c.a);
}
"""
