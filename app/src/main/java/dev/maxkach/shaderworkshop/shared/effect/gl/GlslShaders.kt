package dev.maxkach.shaderworkshop.shared.effect.gl

import org.intellij.lang.annotations.Language
import dev.maxkach.shaderworkshop.shared.effect.ParticleCount

@Language("GLSL")
val UPSIDE_DOWN_VERTEX_SHADER =
  """
    #version 100
    attribute vec4 aFramePosition;
    varying vec2 vTexSamplingCoord;

    void main() {
      gl_Position = aFramePosition;
      vTexSamplingCoord = aFramePosition.xy * 0.5 + 0.5;
    }
"""

@Language("GLSL")
private val SHADER_BODY =
  """
    float random(vec2 seed) {
      return fract(sin(dot(seed, vec2(12.9898, 78.233))) * 43758.5453123);
    }

    float displace(float sliceY, float intensity) {
      float rnd = random(vec2(sliceY, intensity));

      float shouldDisplace = 1.0;
      if (intensity < 0.5 && intensity > rnd * 0.4) {
        shouldDisplace = 0.0;
      }
      return (rnd - 0.5) * 40.0 * intensity * shouldDisplace;
    }

    vec2 scale(vec2 coord, float yMin, float screenWidth, float intensity) {
      float rnd = random(vec2(yMin, intensity));
      if (rnd < intensity) {
        float centerX = screenWidth * 0.5;
        float localX = coord.x - centerX;
        localX /= 1.0 + (intensity * rnd);
        return vec2(centerX + localX, coord.y);
      }
      return coord;
    }

    vec4 getParticle(int id, float time) {
      float seed = float(id);

      float startX = random(vec2(seed, 1.0));
      float startY = random(vec2(seed, 2.0));

      float speedY = (random(vec2(seed, 3.0)) - 0.5) * 0.030;
      float speedX = (random(vec2(seed, 4.0)) - 0.5) * 0.030;

      float size = random(vec2(seed, 5.0)) * 20.0 + 4.0;

      return vec4(fract(startX + speedX * time), fract(startY + speedY * time), size, 1.0);
    }

    float renderParticles(vec2 uv, float time) {
      float particleAlpha = 0.0;

      for (int i = 0; i < MAX_PARTICLES; i++) {
        vec4 particle = getParticle(i, time);
        float dist = length((uv - particle.xy) * uResolution);

        if (dist < particle.z) {
          float alpha = 1.0 - (dist / particle.z);
          particleAlpha = max(particleAlpha, alpha * alpha * 0.6);
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

    vec4 upsideDown(vec2 fragCoord) {
      vec4 originalColor = sampleImage(fragCoord);
      vec3 hsv = rgb2hsv(originalColor.rgb);

      hsv.x = fract(hsv.x + 0.55);
      hsv.y = hsv.y * 0.2;
      hsv.z = hsv.z * (1.0 - uDarknessIntensity * 0.5);

      vec3 adjustedColor = hsv2rgb(hsv);
      adjustedColor = clamp((adjustedColor - 0.5) * 1.9 + 0.5, 0.0, 1.0);
      adjustedColor = mix(adjustedColor, vec3(0.2, 0.3, 0.35), uDarknessIntensity * 0.15);

      vec3 finalColor = adjustedColor;
      if (uUpsideDownAmount > 0.0) {
        float particleAlpha = renderParticles(fragCoord / uResolution, uTime);
        finalColor = mix(adjustedColor, vec3(0.7, 0.7, 0.65), particleAlpha);
      }

      return vec4(mix(originalColor.rgb, finalColor, uUpsideDownAmount), originalColor.a);
    }

    void main() {
      vec2 fragCoord = vec2(vTexSamplingCoord.x, 1.0 - vTexSamplingCoord.y) * uResolution;

      if (uGlitchIntensity <= 0.0 && uUpsideDownAmount <= 0.0) {
        gl_FragColor = sampleImage(fragCoord);
        return;
      }

      if (uGlitchIntensity <= 0.0) {
        gl_FragColor = upsideDown(fragCoord);
        return;
      }

      float sliceHeight = uResolution.y / uSlices;
      float sliceY = floor(fragCoord.y / sliceHeight) * sliceHeight;

      vec2 displaced = fragCoord;
      displaced.x += displace(sliceY, uGlitchIntensity);

      float rnd = random(vec2(uGlitchIntensity * uRealRandom, sliceY));
      if ((rnd * 2.5 + 0.5) < uGlitchIntensity) {
        if (uRealRandom > 0.67) {
          gl_FragColor = yellow;
        } else if (uRealRandom > 0.33) {
          gl_FragColor = red;
        } else {
          gl_FragColor = cyan;
        }
      } else {
        gl_FragColor = upsideDown(scale(displaced, sliceY, uResolution.y, uGlitchIntensity));
      }
    }
"""

@Language("GLSL")
private val TEXTURE_2D_HEAD =
  """
    #version 100
    precision highp float;

    uniform sampler2D uTexSampler;
"""

@Language("GLSL")
private val EXTERNAL_OES_HEAD =
  """
    #version 100
    #extension GL_OES_EGL_image_external : require
    precision highp float;

    uniform samplerExternalOES uTexSampler;
    uniform mat4 uTexMatrix;
    uniform vec2 uCropScale;
"""

@Language("GLSL")
private val COMMON_UNIFORMS =
  """
    uniform vec2 uResolution;
    uniform float uGlitchIntensity;
    uniform float uUpsideDownAmount;
    uniform float uTime;
    uniform float uDarknessIntensity;
    uniform float uRealRandom;
    uniform float uSlices;

    varying vec2 vTexSamplingCoord;

    const int MAX_PARTICLES = $ParticleCount;

    vec4 yellow = vec4(1.0, 1.0, 0.0, 1.0);
    vec4 red = vec4(1.0, 0.0, 0.0, 1.0);
    vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);
"""

@Language("GLSL")
private val SAMPLE_TEXTURE_2D =
  """
    vec4 sampleImage(vec2 fragCoord) {
      vec2 uv = fragCoord / uResolution;
      return texture2D(uTexSampler, vec2(uv.x, 1.0 - uv.y));
    }
"""

@Language("GLSL")
private val SAMPLE_EXTERNAL_OES =
  """
    vec4 sampleImage(vec2 fragCoord) {
      vec2 uv = fragCoord / uResolution;
      vec2 tex = vec2(uv.x, 1.0 - uv.y);
      tex = (tex - 0.5) * uCropScale + 0.5;
      return texture2D(uTexSampler, (uTexMatrix * vec4(tex, 0.0, 1.0)).xy);
    }
"""

val UPSIDE_DOWN_FRAGMENT_SHADER_2D: String =
  TEXTURE_2D_HEAD + COMMON_UNIFORMS + SAMPLE_TEXTURE_2D + SHADER_BODY

val UPSIDE_DOWN_FRAGMENT_SHADER_OES: String =
  EXTERNAL_OES_HEAD + COMMON_UNIFORMS + SAMPLE_EXTERNAL_OES + SHADER_BODY
