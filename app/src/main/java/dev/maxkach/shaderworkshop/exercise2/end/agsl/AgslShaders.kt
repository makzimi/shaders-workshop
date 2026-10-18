package dev.maxkach.shaderworkshop.exercise2.end.agsl

import org.intellij.lang.annotations.Language

@Suppress("LongMethod")
fun upsideDownShader(particleCount: Int): String =
  """
    uniform shader image;
    uniform float2 imageSize;
    uniform float time;
    uniform float darknessIntensity;

    const int MAX_PARTICLES = $particleCount;

    float random(float2 seed) {
        return fract(sin(dot(seed, float2(12.9898, 78.233))) * 43758.5453123);
    }

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

    vec3 rgb2hsv(vec3 c) {
        vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
        vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
        vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

        float d = q.x - min(q.w, q.y);
        float e = 1.0e-10;
        return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
    }

    vec3 hsv2rgb(vec3 c) {
        vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
        vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
        return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
    }

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / imageSize;

        half4 originalColor = image.eval(fragCoord);
        vec3 color = originalColor.rgb;

        vec3 hsv = rgb2hsv(color);

        hsv.x = hsv.x + 0.55;
        if (hsv.x > 1.0) hsv.x -= 1.0;

        hsv.y = hsv.y * 0.2;

        hsv.z = hsv.z * (1.0 - darknessIntensity * 0.5);

        vec3 adjustedColor = hsv2rgb(hsv);

        adjustedColor = (adjustedColor - 0.5) * 1.9 + 0.5;
        adjustedColor = clamp(adjustedColor, 0.0, 1.0);

        vec3 tintColor = vec3(0.2, 0.3, 0.35);
        adjustedColor = mix(adjustedColor, tintColor, darknessIntensity * 0.15);

        float particleAlpha = renderParticles(uv, time);

        vec3 particleColor = vec3(0.7, 0.7, 0.65);

        vec3 finalColor = mix(adjustedColor, particleColor, particleAlpha);

        return half4(finalColor * originalColor.a, originalColor.a);
    }
"""

@Language("AGSL")
val GLITCH_SHADER =
  """
    uniform shader image;
    uniform float2 imageSize;
    uniform float intensity;
    uniform int slices;
    uniform float realRandom;

    vec4 yellow = vec4(1.0, 1.0, 0.0, 1.0);
    vec4 red = vec4(1.0, 0.0, 0.0, 1.0);
    vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);

    float random(float seed) {
        return fract(sin(seed) * 100000.0);
    }

    float random(float2 seed) {
        return fract(sin(dot(seed, float2(12.9898, 78.233))) * 43758.5453123);
    }

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
    half4 main(float2 fragCoord) {

        float sliceHeight = imageSize.y / float(slices);
        float sliceY = floor(fragCoord.y / sliceHeight) * sliceHeight;
        float displace = displace(sliceY, intensity);
        float2 displaced = fragCoord;
        displaced.x += displace;
        float rnd = random(float2(intensity * realRandom, sliceY));
        if ((rnd * 2.5 + 0.5) < intensity) {
           if (realRandom > 0.67) {
               return yellow;
           } else if (realRandom > 0.33) {
               return red;
           } else {
               return cyan;
           }
        } else {
           float2 scaled = scale(displaced, sliceY, sliceY + sliceHeight, imageSize.y, intensity);
           return image.eval(scaled);
        }
    }
"""
