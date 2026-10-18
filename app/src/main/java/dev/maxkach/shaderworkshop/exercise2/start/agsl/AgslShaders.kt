package dev.maxkach.shaderworkshop.exercise2.start.agsl

import org.intellij.lang.annotations.Language

@Suppress("UNUSED_PARAMETER", "LongMethod")
@Language("AGSL")
fun upsideDownShader(particleCount: Int = 0): String = """
uniform shader image;
uniform float2 imageSize;
uniform float time;
uniform float darknessIntensity;
// STEP-BEGIN grade-1: Prepare the optional particle count and random helper.
/*
const int MAX_PARTICLES = $particleCount;

float random(float2 seed) {
    return fract(sin(dot(seed, float2(12.9898, 78.233))) * 43758.5453123);
}
*/
// STEP-BEGIN grade-2: Prepare the original optional particle motion.
/*

float4 getParticle(int id, float time) {
    float seed = float(id);

    float startX = random(float2(seed, 1.0));
    float startY = random(float2(seed, 2.0));

    float speedY = (random(float2(seed, 3.0)) - 0.5) * 0.030;
    float speedX = (random(float2(seed, 4.0)) - 0.5) * 0.030;

    float size = random(float2(seed, 5.0)) * 20.0 + 4.0;

    float x = fract(startX + speedX * time);
    float y = fract(startY + speedY * time);

    return float4(x, y, size, 1.0);
}
*/
// STEP-BEGIN grade-3: Prepare the optional soft particle sampling.
/*

float renderParticles(float2 uv, float time) {
    float particleAlpha = 0.0;

    for (int i = 0; i < MAX_PARTICLES; i++) {
        float4 particle = getParticle(i, time);
        float2 particlePos = particle.xy;
        float particleSize = particle.z;

        float2 diff = (uv - particlePos) * imageSize;
        float dist = length(diff);

        if (dist < particleSize) {
            float alpha = 1.0 - (dist / particleSize);
            alpha = alpha * alpha;
            particleAlpha = max(particleAlpha, alpha * 0.6);
        }
    }

    return particleAlpha;
}
*/
// STEP-BEGIN grade-4: Convert RGB to HSV with the original helper.
/*

vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}
*/
// STEP-BEGIN grade-5: Convert HSV back to RGB with the original helper.
/*

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}
*/
half4 main(float2 fragCoord) {
    half4 result = image.eval(fragCoord);
    // STEP-BEGIN grade-6: Normalize coordinates for optional particles.
    /*
    float2 uv = fragCoord / imageSize;
    */
    // STEP-BEGIN grade-7: Sample the original image and its RGB.
    /*

    half4 originalColor = image.eval(fragCoord);
    vec3 color = originalColor.rgb;
    */
    // STEP-BEGIN grade-8: Convert the sampled RGB to HSV.
    /*

    vec3 hsv = rgb2hsv(color);
    */
    // STEP-BEGIN grade-9: Shift hue toward blue/cyan and wrap it.
    /*

    hsv.x = hsv.x + 0.55;
    if (hsv.x > 1.0) hsv.x -= 1.0;

    result = half4(hsv2rgb(hsv) * originalColor.a, originalColor.a);
    */
    // STEP-BEGIN grade-10: Scale saturation by the original 0.2 factor.
    /*

    hsv.y = hsv.y * 0.2;

    result = half4(hsv2rgb(hsv) * originalColor.a, originalColor.a);
    */
    // STEP-BEGIN grade-11: Apply darkness to brightness. Try darknessIntensity in the prepared state.
    /*

    hsv.z = hsv.z * (1.0 - darknessIntensity * 0.5);

    result = half4(hsv2rgb(hsv) * originalColor.a, originalColor.a);
    */
    // STEP-BEGIN grade-12: Convert the adjusted HSV to RGB.
    /*

    vec3 adjustedColor = hsv2rgb(hsv);

    result = half4(adjustedColor * originalColor.a, originalColor.a);
    */
    // STEP-BEGIN grade-13: Increase contrast by 1.9 and clamp.
    /*

    adjustedColor = (adjustedColor - 0.5) * 1.9 + 0.5;
    adjustedColor = clamp(adjustedColor, 0.0, 1.0);

    result = half4(adjustedColor * originalColor.a, originalColor.a);
    */
    // STEP-BEGIN grade-14: Blend the original subtle blue-green tint.
    /*

    vec3 tintColor = vec3(0.2, 0.3, 0.35);
    adjustedColor = mix(adjustedColor, tintColor, darknessIntensity * 0.15);

    result = half4(adjustedColor * originalColor.a, originalColor.a);
    */
    // STEP-BEGIN grade-15: Evaluate optional particles. Zero keeps the prepared overlay as their owner.
    /*

    float particleAlpha = renderParticles(uv, time);
    */
    // STEP-BEGIN grade-16: Prepare the original dust color.
    /*

    vec3 particleColor = vec3(0.7, 0.7, 0.65);
    */
    // STEP-BEGIN grade-17: Composite grade and optional particles.
    /*

    vec3 finalColor = mix(adjustedColor, particleColor, particleAlpha);

    result = half4(finalColor * originalColor.a, originalColor.a);
    */
    // STEP-BEGIN grade-18: Preserve alpha and return premultiplied RGB.
    /*

    result = half4(finalColor * originalColor.a, originalColor.a);
    */
    return result;
}
"""

@Language("AGSL")
val GLITCH_SHADER = """
uniform shader image;
uniform float2 imageSize;
uniform float intensity;
uniform int slices;
uniform float realRandom;
// STEP-BEGIN glitch-1: Prepare the original stripe colors.
/*
vec4 yellow = vec4(1.0, 1.0, 0.0, 1.0);
vec4 red = vec4(1.0, 0.0, 0.0, 1.0);
vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);
*/
// STEP-BEGIN glitch-2: Prepare the two deterministic random helpers.
/*

float random(float seed) {
    return fract(sin(seed) * 100000.0);
}

float random(float2 seed) {
    return fract(sin(dot(seed, float2(12.9898, 78.233))) * 43758.5453123);
}
*/
// STEP-BEGIN glitch-3: Compute each stripe's horizontal displacement.
/*

float displace(float sliceY, float intensity) {
    float rnd = random(float2(sliceY, intensity));

    float shouldDisplace = 0.0;
    if (intensity < 0.5 && intensity > rnd * 0.4) {
        shouldDisplace = 0.0;
    } else {
        shouldDisplace = 1.0;
    }
    return (rnd - 0.5) * 40.0 * intensity * shouldDisplace;
}
*/
// STEP-BEGIN glitch-4: Scale a displaced stripe around the center.
/*
float2 scale(float2 coord, float yMin, float yMax, float screenWidth, float intensity) {
    float rnd = random(float2(yMin, intensity));
    if (rnd < intensity) {
        float centerX = screenWidth * 0.5;

        float localX = coord.x - centerX;
        float scaleFactor = 1.0 + (intensity * rnd);
        localX /= scaleFactor;

        float scaledX = centerX + localX;

        return float2(scaledX, coord.y);
    }

    return coord;
}
*/
half4 main(float2 fragCoord) {
    half4 result = image.eval(fragCoord);
    // STEP-BEGIN glitch-5: Find the horizontal slice and displaced sample coordinates.
    /*

    float sliceHeight = imageSize.y / float(slices);
    float sliceY = floor(fragCoord.y / sliceHeight) * sliceHeight;
    float displace = displace(sliceY, intensity);
    float2 displaced = fragCoord;
    displaced.x += displace;
    float rnd = random(float2(intensity * realRandom, sliceY));
    */
    // STEP-BEGIN glitch-6: Show solid bands or sample the displaced/scaled image.
    /*
    if ((rnd * 2.5 + 0.5) < intensity) {
        if (realRandom > 0.67) {
            result = yellow;
        } else if (realRandom > 0.33) {
            result = red;
        } else {
            result = cyan;
        }
    } else {
        float2 scaled = scale(displaced, sliceY, sliceY + sliceHeight, imageSize.y, intensity);
        result = image.eval(scaled);
    }
    */
    return result;
}
"""
