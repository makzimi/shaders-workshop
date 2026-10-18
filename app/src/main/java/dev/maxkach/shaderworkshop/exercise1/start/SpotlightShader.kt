package dev.maxkach.shaderworkshop.exercise1.start

import org.intellij.lang.annotations.Language

@Language("AGSL")
const val spotlightSource = """
uniform shader content;
uniform float2 resolution;
uniform float time;
half4 main(float2 p) {
    half4 c = content.eval(p);
    // STEP-BEGIN spotlight-1: Normalize pixel coordinates.
    /*
    float2 uv = p / resolution;
    */
    // STEP-BEGIN spotlight-2: Move the center with time. Try 0.25 for the horizontal travel.
    /*
    float2 center = float2(0.5 + 0.25 * sin(time), 0.5);
    */
    // STEP-BEGIN spotlight-3: Correct the aspect ratio before measuring distance.
    /*
    float2 delta = (uv - center) * float2(resolution.x / resolution.y, 1.0);
    */
    // STEP-BEGIN spotlight-4: Make a soft edge. Try changing the 0.08 core or 0.35 outer radius.
    /*
    float light = 1.0 - smoothstep(0.08, 0.35, length(delta));
    */
    // STEP-BEGIN spotlight-5: Dim RGB while preserving sampled alpha. Try changing the 0.25 brightness floor.
    /*
    c.rgb = c.rgb * (0.25 + 0.75 * light);
    */
    return c;
}
"""
