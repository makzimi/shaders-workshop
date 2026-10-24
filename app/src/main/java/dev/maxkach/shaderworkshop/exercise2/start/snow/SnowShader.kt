package dev.maxkach.shaderworkshop.exercise2.start.snow

import org.intellij.lang.annotations.Language

@Language("AGSL")
val SNOW_SHADER = """
    uniform float2 size;
    uniform float time;

    // Turn a grid cell into two stable random numbers between 0 and 1.
    //float2 hash2(float2 p) {
    //    float3 p3 = fract(float3(p.xyx) * float3(0.1031, 0.1030, 0.0973));
    //    p3 += dot(p3, p3.yzx + 33.33);
    //    return fract(float2((p3.x + p3.y) * p3.z, (p3.y + p3.z) * p3.x));
    //}

    float snowLayer(float2 p, float t, float scale, float2 velocity) {
        float brightness = 0.0;

        // Split the screen into a moving grid. Find this pixel's cell and its position inside it.
        //float2 q = p * scale - velocity * t;
        //float2 cell = floor(q);
        //float2 f = q - cell;

        // Give each cell one random flake center and let it sway sideways. Try changing the 0.08 sway.
        //float2 rnd = hash2(cell);
        //float2 center = float2(0.2) + 0.6 * rnd;
        //center.x += 0.08 * sin(t * (0.6 + rnd.y) + rnd.x * 6.2831);

        // Measure the distance to the center: a bright core plus a soft halo.
        //float radius = 0.05 + 0.1 * rnd.y;
        //float d = length(f - center);
        //float core = smoothstep(radius, radius * 0.2, d);
        //float halo = smoothstep(radius * 2.4, 0.0, d) * 0.25;
        //brightness = core + halo;

        // Give every flake its own brightness. Try changing the 0.35 floor.
        //brightness *= 0.35 + 0.65 * rnd.x;

        return brightness;
    }

    half4 main(float2 fragCoord) {
        // Normalize coordinates and correct the aspect ratio so the flakes stay round.
        float2 unit = fragCoord / max(size, float2(1.0));
        float aspect = size.x / max(size.y, 1.0);
        float2 p = float2(unit.x * aspect, unit.y);

        // First layer: large, fast flakes.
        float flakes = 0.8 * snowLayer(p, time, 8.0, float2(0.05, 0.22));

        // Add two smaller, slower layers for depth.
        //flakes += 0.9 * snowLayer(p, time + 40.0, 14.0, float2(0.03, 0.1));
        //flakes += 0.7 * snowLayer(p, time + 80.0, 22.0, float2(-0.04, 0.16));

        // Return white, premultiplied by the flake alpha.
        half a = half(clamp(flakes, 0.0, 1.0) * 0.9);
        return half4(a, a, a, a);
    }
"""
