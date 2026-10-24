package dev.maxkach.shaderworkshop.exercise2.start.vines

import org.intellij.lang.annotations.Language

@Language("AGSL")
val VINES_VERTEX_SHADER = """
    uniform float2 screenSize;
    uniform float progress;
    uniform float mainWidth;
    uniform float pointCount;

    Varyings main(const Attributes attributes) {
        Varyings varyings;
        float2 p = attributes.position;
        float2 d = float2(1.0, 0.0);
        float halfWidth = 0.0;
        // STEP-BEGIN vines-1: Turn progress into how many centerline points have grown.
        /*
        float maxLen = length(screenSize) * 0.53;
        float targetLen = maxLen * progress;
        float steps = targetLen / 4.0;
        float grown = steps > 0.0 ? ceil(steps) : 0.0;
        float validPoints = min(pointCount, 1.0 + grown);
        */
        // STEP-BEGIN vines-2: Read this vertex's point index and its side of the ribbon.
        /*
        float i = attributes.meta.x;
        float side = attributes.meta.y;
        */
        // STEP-BEGIN vines-3: Fold points that have not grown yet onto the previous point.
        /*
        bool isGrown = i < validPoints;
        if (!isGrown) {
            p = attributes.prev;
        }
        */
        // STEP-BEGIN vines-4: Point grown vertices along the vine using their neighbours.
        /*
        if (isGrown) {
            if (i == 0.0 && validPoints > 1.0) {
                d = attributes.next - p;
            } else if (i == validPoints - 1.0 && i > 0.0) {
                d = p - attributes.prev;
            } else if (i > 0.0 && i < validPoints - 1.0) {
                d = attributes.next - attributes.prev;
            }
        }
        */
        // STEP-BEGIN vines-5: Taper the width from the thick root to a thin tip.
        /*
        if (isGrown) {
            float t = i / max(1.0, validPoints - 1.0);
            halfWidth = mix(mainWidth, 6.0, t) * 0.5;
        }
        */
        // STEP-BEGIN vines-6: Push each vertex sideways along the normal to give the ribbon its width.
        /*
        float2 nrm = length(d) > 0.001 ? float2(-d.y, d.x) / length(d) : float2(0.0, 1.0);
        p = p + nrm * halfWidth * side;
        */
        varyings.position = p;
        return varyings;
    }
"""

@Language("AGSL")
val VINES_FRAGMENT_SHADER = """
    float2 main(const Varyings varyings, out float4 color) {
        color = float4(0.0, 0.0, 0.0, 0.92);
        return varyings.position;
    }
"""
