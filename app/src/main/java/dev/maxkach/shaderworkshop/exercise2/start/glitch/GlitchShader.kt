package dev.maxkach.shaderworkshop.exercise2.start.glitch

import org.intellij.lang.annotations.Language

@Language("AGSL")
val GLITCH_SHADER = """
uniform shader image;
uniform float2 imageSize;
uniform float intensity;
uniform int slices;
uniform float realRandom;
// Prepare the original stripe colors.
//vec4 yellow = vec4(1.0, 1.0, 0.0, 1.0);
//vec4 red = vec4(1.0, 0.0, 0.0, 1.0);
//vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);

// Prepare the deterministic random helper.
//float random(float2 seed) {
//    return fract(sin(dot(seed, float2(12.9898, 78.233))) * 43758.5453123);
//}

// Compute each stripe's horizontal displacement.
//float displace(float sliceY, float intensity) {
//    float rnd = random(float2(sliceY, intensity));
//
//    float shouldDisplace = 0.0;
//    if (intensity < 0.5 && intensity > rnd * 0.4) {
//        shouldDisplace = 0.0;
//    } else {
//        shouldDisplace = 1.0;
//    }
//    return (rnd - 0.5) * 40.0 * intensity * shouldDisplace;
//}

// Scale a displaced stripe around the center.
//float2 scale(float2 coord, float yMin, float yMax, float screenWidth, float intensity) {
//    float rnd = random(float2(yMin, intensity));
//    if (rnd < intensity) {
//        float centerX = screenWidth * 0.5;
//
//        float localX = coord.x - centerX;
//        float scaleFactor = 1.0 + (intensity * rnd);
//        localX /= scaleFactor;
//
//        float scaledX = centerX + localX;
//
//        return float2(scaledX, coord.y);
//    }
//
//    return coord;
//}

half4 main(float2 fragCoord) {
    half4 result = image.eval(fragCoord);
    
    // Find the horizontal slice and displaced sample coordinates.
//    float sliceHeight = imageSize.y / float(slices);
//    float sliceY = floor(fragCoord.y / sliceHeight) * sliceHeight;
//    float displace = displace(sliceY, intensity);
//    float2 displaced = fragCoord;
//    displaced.x += displace;
//    float rnd = random(float2(intensity * realRandom, sliceY));
    
    // Show solid bands or sample the displaced/scaled image.
//    if ((rnd * 2.5 + 0.5) < intensity) {
//        if (realRandom > 0.67) {
//            result = yellow;
//        } else if (realRandom > 0.33) {
//            result = red;
//        } else {
//            result = cyan;
//        }
//    } else {
//        float2 scaled = scale(displaced, sliceY, sliceY + sliceHeight, imageSize.y, intensity);
//        result = image.eval(scaled);
//    }

    // Outside the solid bands, read red a little to the right so the red image moves left. Try changing the 0.035 shift.
//    if ((rnd * 2.5 + 0.5) >= intensity) {
//        float2 scaled = scale(displaced, sliceY, sliceY + sliceHeight, imageSize.y, intensity);
//        half4 shifted = image.eval(scaled + float2(imageSize.x * 0.035 * intensity, 0.0));
//        result.r = shifted.r / max(shifted.a, 0.001) * result.a;
//    }

    return result;
}
"""
