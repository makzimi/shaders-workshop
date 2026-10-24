# Live-coding cue sheet

Run the app. Show **Finish**, press Back, then open **Start** for the exercise. Edit Start only. Remove `/*` and `*/` under each numbered cue; leave the code and final return in place. Run after each row. Tap the card's effect icon after attaching the shader.

Exercise 1 uses the RedByteFX Kotlin DSL in both `start/` and `end/`. The host and typed time uniform are supplied setup. Each commented Kotlin step builds on the previous one. The original AGSL comparison lives in `examples/AgslSpotlight.kt`.

Exercise 2 follows **color grading → glitch → vines**. Helpers and the snow/vine attachments are supplied setup.

Paths are inside `app/src/main/java/dev/maxkach/shaderworkshop/`.

| Step | Say | Open / do |
| --- | --- | --- |
| **1 · Spotlight, 7 min** | “Connect a shader that returns the original pixel.” | `exercise1/start/SpotlightEffect.kt`: `spotlight-attach-1`. |
| 2 | “Normalize pixels, then move the light with time.” | `exercise1/start/SpotlightShader.kt`: `spotlight-1–2`. |
| 3 | “Correct the aspect ratio so the light stays circular.” | Same file: `spotlight-3`. |
| 4 | “Smoothstep gives the light a soft edge.” | Same file: `spotlight-4`. Try the `0.35f` radius. |
| 5 | “Dim the outside, but preserve alpha.” | Same file: `spotlight-5`. Try the `0.25f` brightness floor. |
| **6 · Upside Down, 10 min** | “Start again from the clean card.” | Back → **Upside Down: Start**. `exercise2/start/grading/GradingShader.kt`: `grade-1–2` HSV helpers. |
| 7 | “Attach the prepared snow, vines, glitch and grading layers.” | `exercise2/start/WorkshopEffect.kt`: `upside-attach-1–3`; tap the effect icon. |
| 8 | “Sample the pixel and convert RGB to HSV.” | `exercise2/start/grading/GradingShader.kt`: `grade-3–4`. |
| 9 | “Shift hue, reduce saturation and darken.” | Same file: `grade-5–7`, preview each. |
| 10 | “Add contrast and a cool tint, preserving alpha.” | Same file: `grade-8–10`. The snow overlay draws the particles. |
| 11 | “Displace and scale horizontal slices, then add solid bands.” | `exercise2/start/glitch/GlitchShader.kt`: `glitch-1–6`; toggle off/on to replay. |
| 12 | “Grow the vines: count grown points, fold the rest, then taper and widen the ribbon.” | `exercise2/start/vines/VinesShader.kt`, `VINES_VERTEX_SHADER`: `vines-1–6`. The ribbon appears at `vines-6`. Mesh vines need Android 14+; older devices show the Canvas fallback. |

Recovery: Back → the exercise's **Finish**. Its matching `end/` folder contains the working answer. Snow and video renderers are prepared code; no additional live coding is scheduled.
