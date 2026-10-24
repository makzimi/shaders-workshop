# Live-coding cue sheet

Run the app. Show **Finish**, press Back, then open **Start** for the exercise. Edit Start only. Remove `/*` and `*/` under each numbered cue; leave the code and final return in place. Run after each row. Tap the card's effect icon after attaching the shader.

Exercise 1 uses the RedByteFX Kotlin DSL in both `start/` and `end/`. The host and typed time uniform are supplied setup. Each commented Kotlin step builds on the previous one. The original AGSL comparison lives in `examples/AgslSpotlight.kt`.

Exercise 2 follows **color grading → glitch → snow → vines**. Helpers and the snow/vine attachments are supplied setup.

Paths are inside `app/src/main/java/dev/maxkach/shaderworkshop/`.

| Step | Say | Open / do |
| --- | --- | --- |
| **1 · Spotlight, 7 min** | “Connect a shader that returns the original pixel.” | `exercise1/start/SpotlightEffect.kt`: `spotlight-attach-1`. |
| 2 | “Normalize pixels, then move the light with time.” | `exercise1/start/SpotlightShader.kt`: `spotlight-1–2`. |
| 3 | “Correct the aspect ratio so the light stays circular.” | Same file: `spotlight-3`. |
| 4 | “Smoothstep gives the light a soft edge.” | Same file: `spotlight-4`. Try the `0.35f` radius. |
| 5 | “Dim the outside, but preserve alpha.” | Same file: `spotlight-5`. Try the `0.25f` brightness floor. |
| **6 · Upside Down, 10 min** | “Start again from the clean card.” | Back → **Upside Down: Start**. `exercise2/start/grading/GradingShader.kt` is complete code: walk through the HSV helpers. |
| 7 | “Attach the prepared snow, vines, glitch and grading layers.” | `exercise2/start/WorkshopEffect.kt`: `upside-attach-1–3`; tap the effect icon. |
| 8 | “Sample the pixel and convert RGB to HSV.” | `exercise2/start/grading/GradingShader.kt`: the sampling and HSV comments. |
| 9 | “Shift hue, reduce saturation and darken.” | Same file: the hue, saturation and darkness comments. |
| 10 | “Add contrast and a cool tint, preserving alpha.” | Same file: the contrast and tint comments. The snow overlay draws the particles. |
| 11 | “Displace and scale horizontal slices, add solid bands, then read red a little to the right so it splits off like an old VHS tape.” | `exercise2/start/glitch/GlitchShader.kt`: uncomment each `//` block under its explanation, top to bottom; the last block adds the red shift. Toggle off/on to replay. |
| 12 | “Each pixel finds its grid cell, measures the distance to that cell's flake, then we stack three layers.” | `exercise2/start/snow/SnowShader.kt`: uncomment each `//` block under its explanation, top to bottom. Flakes appear once the core and halo block is on; the last block adds the two far layers. |
| 13 | “Grow the vines: count grown points, fold the rest, then taper and widen the ribbon.” | `exercise2/start/vines/VinesShader.kt`, `VINES_VERTEX_SHADER`: `vines-1–6`. The ribbon appears at `vines-6`. Mesh vines need Android 14+; older devices show the Canvas fallback. |

Recovery: Back → the exercise's **Finish**. Its matching `end/` folder contains the working answer. Video renderers are prepared code; no additional live coding is scheduled.
