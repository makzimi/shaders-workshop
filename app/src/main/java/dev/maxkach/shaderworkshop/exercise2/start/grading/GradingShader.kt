package dev.maxkach.shaderworkshop.exercise2.start.grading

import org.intellij.lang.annotations.Language

@Language("AGSL")
val GRADING_SHADER = """
uniform shader image;
uniform float darknessIntensity;
// Convert RGB to HSV with the original helper.
//vec3 rgb2hsv(vec3 c) {
//    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
//    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
//    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
//
//    float d = q.x - min(q.w, q.y);
//    float e = 1.0e-10;
//    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
//}

// Convert HSV back to RGB with the original helper.
//vec3 hsv2rgb(vec3 c) {
//    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
//    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
//    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
//}

half4 main(float2 fragCoord) {
    half4 result = image.eval(fragCoord);
    
    // Sample the original image and its RGB.
//    half4 originalColor = image.eval(fragCoord);
//    vec3 color = originalColor.rgb;

    // Convert the sampled RGB to HSV.
//    vec3 hsv = rgb2hsv(color);

    // Shift hue toward blue/cyan and wrap it.
//    hsv.x = hsv.x + 0.55;
//    if (hsv.x > 1.0) hsv.x -= 1.0;
//    result = half4(hsv2rgb(hsv) * originalColor.a, originalColor.a);

    // Scale saturation by the original 0.2 factor.
//    hsv.y = hsv.y * 0.2;
//    result = half4(hsv2rgb(hsv) * originalColor.a, originalColor.a);
    
    // Apply darkness to brightness. Try darknessIntensity in the prepared state.
//    hsv.z = hsv.z * (1.0 - darknessIntensity * 0.5);
//    result = half4(hsv2rgb(hsv) * originalColor.a, originalColor.a);

    // Convert the adjusted HSV to RGB.
//    vec3 adjustedColor = hsv2rgb(hsv);
//    result = half4(adjustedColor * originalColor.a, originalColor.a);
    
    // Increase contrast by 1.9 and clamp.
//    adjustedColor = (adjustedColor - 0.5) * 1.9 + 0.5;
//    adjustedColor = clamp(adjustedColor, 0.0, 1.0);
//    result = half4(adjustedColor * originalColor.a, originalColor.a);
    
    // Blend the original subtle blue-green tint, preserving alpha.
//    vec3 tintColor = vec3(0.2, 0.3, 0.35);
//    adjustedColor = mix(adjustedColor, tintColor, darknessIntensity * 0.15);
//    result = half4(adjustedColor * originalColor.a, originalColor.a);
    
    return result;
}
"""
