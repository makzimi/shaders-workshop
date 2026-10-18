# Live-coding cue sheet

Run the app. Show **Finish**, press Back, then open **Start** for the exercise. Edit Start only. Remove `/*` and `*/` under each numbered cue; leave the code and final return in place. Run after each row. Tap the card's effect icon after attaching the shader.

Exercise 2 follows **color grading → glitch**. Helpers and snow/vine attachments are supplied setup.

Paths are inside `app/src/main/java/dev/maxkach/shaderworkshop/`.

| Step | Say | Open / do |
| --- | --- | --- |
| **1 · Spotlight, 7 min** | “Connect a shader that returns the original pixel.” | `exercise1/start/SpotlightEffect.kt`: `spotlight-attach-1`. |
| 2 | “Normalize pixels, then move the light with time.” | `exercise1/start/SpotlightShader.kt`: `spotlight-1–2`. |
| 3 | “Correct the aspect ratio so the light stays circular.” | Same file: `spotlight-3`. |
| 4 | “Smoothstep gives the light a soft edge.” | Same file: `spotlight-4`. Try the `0.35` radius. |
| 5 | “Dim the outside, but preserve alpha.” | Same file: `spotlight-5`. Try the `0.25` brightness floor. |
| **6 · Upside Down, 10 min** | “Start again from the clean card.” | Back → **Upside Down: Start**. `exercise2/start/agsl/AgslShaders.kt`, top `upsideDownShader` block: `grade-1–5` helpers. |
| 7 | “Attach the prepared snow, vines and AGSL layer.” | `exercise2/start/WorkshopEffect.kt`: `upside-attach-1–3`; tap the effect icon. |
| 8 | “Sample the pixel and convert RGB to HSV.” | `exercise2/start/agsl/AgslShaders.kt`: `grade-6–8`. |
| 9 | “Shift hue, reduce saturation and darken.” | Same file: `grade-9–11`, preview each. |
| 10 | “Add contrast and a cool tint, preserving alpha.” | Same file: `grade-12–18`. Optional shader particles stay at zero; the snow overlay draws them. |
| 11 | “Displace and scale horizontal slices, then add solid bands.” | Same file, below grading in `GLITCH_SHADER`: `glitch-1–6`; toggle off/on to replay. |

Recovery: Back → the exercise's **Finish**. Its matching `end/` folder contains the working answer. Snow, vines and video renderers are prepared code; no additional live coding is scheduled.
