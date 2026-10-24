# Shaders Workshop

Live-coding project for Max's **Stranger Shaders** workshop at DevFest Baku. Build Android shader effects with Kotlin, Jetpack Compose and AGSL, starting with a spotlight and progressing to color grading and glitch.

## Get started

```sh
git clone https://github.com/makzimi/shaders-workshop.git
cd shaders-workshop
```

Open this folder in Android Studio, let Gradle sync, and run **app** on Android 13 / API 33 or newer. Use **JDK 17** and **Android SDK 36**. Initial dependency downloads need an internet connection; the app's media is bundled.

## Choose an exercise

The app opens with four buttons. Show **Finish** to see the goal, press Back, then open **Start** to code. The card's effect icon toggles the shader. Each exercise resets when reopened.

| Exercise | Start | Finish |
| --- | --- | --- |
| 1. Spotlight | [Clean card and numbered steps](app/src/main/java/dev/maxkach/shaderworkshop/exercise1/start) | [Animated spotlight](app/src/main/java/dev/maxkach/shaderworkshop/exercise1/end) |
| 2. Upside Down | [Independent clean card and numbered steps](app/src/main/java/dev/maxkach/shaderworkshop/exercise2/start) | [Color grading, glitch, snow and vines](app/src/main/java/dev/maxkach/shaderworkshop/exercise2/end) |

Follow the [compact live-coding guide](LIVE-CODING.md). Remove the block-comment delimiters under each numbered cue. Matching `end/` files contain complete answers. For exercise 2, enable the supplied helpers and attachments, then work through **color grading → glitch**. Snow, vines and video infrastructure are prepared code.

## Exercise 1 uses Kotlin shaders

Both Exercise 1 packages use the [RedByteFX](https://github.com/i-redbyte/redbytefx) Kotlin DSL. Run **app** and choose **Spotlight: Start** for the commented exercise or **Spotlight: Finish** for the completed effect. Start initially shows the clean card; remove the numbered comment delimiters and tap the effect icon to try each step.

The **Spotlight Kotlin DSL** Android Studio run configuration opens the finished effect directly. Select **Spotlight AGSL** to compare the retained string implementation. Both configurations return to the four-button exercise launcher with Back.

Edit [exercise1/start/SpotlightShader.kt](app/src/main/java/dev/maxkach/shaderworkshop/exercise1/start/SpotlightShader.kt) during the workshop. The complete answer is in [exercise1/end/SpotlightShader.kt](app/src/main/java/dev/maxkach/shaderworkshop/exercise1/end/SpotlightShader.kt). Try changing `smoothstep(0.08f, 0.35f, ...)` to adjust the light radius, or `0.25f + 0.75f * light` to change its brightness. The `float2`, `sin`, `length`, operators and uniform handles are Kotlin APIs, so the IDE can complete and type-check their use.

From a terminal, with `adb` on your PATH and one device connected:

```sh
./gradlew :app:installDebug
adb shell am start -W -S -n dev.maxkach.shaderworkshop/.MainActivity --es example FirstShaderKotlin --ez effect true
```

To launch the original AGSL version:

```sh
adb shell am start -W -S -n dev.maxkach.shaderworkshop/.MainActivity --es example FirstShader --ez effect true
```

Alternatively, set `exampleToRun` in `MainActivity.kt` to `WorkshopExample.FirstShaderKotlin` and tap the card's effect icon after launching. Add `--ez fixed true` to either command-line route to freeze the effect at the same time for comparison.

### How the Kotlin version works

| Original AGSL | Kotlin DSL |
| --- | --- |
| `examples/AgslSpotlight.kt` | `exercise1/start/SpotlightShader.kt` and `exercise1/end/SpotlightShader.kt` |
| `uniform float time` | `uniformTime(0f, "time")`, returning a typed handle |
| `p / resolution` | `fragCoord / resolution` |
| `content.eval(p)` | `sample()` |
| `half4(c.rgb * gain, c.a)` | `color(c.r * gain, c.g * gain, c.b * gain, c.a)` |

`redbytefx { ... }` builds an effect description and generates AGSL. Each exercise's `SpotlightEffect.kt` remembers that description. `ShaderHost.kotlinEffect` remembers its runtime instance, supplies the measured resolution and animated time, and attaches its `RenderEffect` to the Compose layer. The spotlight math, product card and animation state are shared with the string example. The DSL's `sample()` clamps coordinates; this spotlight samples at the original pixel, so that does not change its intended result.

This is a typed shader builder, not a compiler for arbitrary Kotlin functions running on the GPU. Kotlin checks API calls and expression types during the app build. DSL validation happens when the effect is built, and Android compiles the generated AGSL when `newInstance()` creates its `RuntimeShader`. Device compilation and rendering checks still matter. Kotlin breakpoints inspect the builder and host, not individual GPU pixels. To inspect the generated shader, evaluate `spotlight.effect.agslSource()` at a breakpoint in `SpotlightEffect.kt` after `spotlight` is initialized.

### Library choice and scope

The project pins `io.github.i-redbyte:redbytefx-core:1.0.0` from Maven Central and supplies its own small Compose adapter. No compiler plugin or additional Compose wrapper is needed for this example. The [upstream README](https://github.com/i-redbyte/redbytefx#readme) now demonstrates the 1.2.0 API (`shader(ShaderTarget.Agsl)`, `ShaderProgram`); those snippets differ from the pinned API used here. Treat a version upgrade as a separate migration.

[ORSL](https://github.com/openrndr/orsl) also provides Kotlin shader builders but depends on OPENRNDR/ORX. [Kool's KSL](https://kool-engine.github.io/docs/shading.html) generates GLSL/WGSL for its rendering engine. RedByteFX is the closest fit for this sample's Android `RuntimeShader` and Compose layer pipeline.

Exercise 2 uses separate grading and glitch AGSL effects, with supplied snow and mesh vines. The video examples additionally contain GLSL for Media3 and raw OpenGL rendering. Those paths need their own migration assessment. The first exercise is a small, useful place to evaluate the DSL before expanding it.

The existing `WorkshopReferenceTest` checks that both spotlight versions compile on Android and compares their rendered product cards at a fixed time. Its pixel comparison is a tolerant check of the red channel, not a guarantee of exact equality across all pixels, channels and devices.

## Find your way around

Inside `app/src/main/java/dev/maxkach/shaderworkshop/`:

```text
exercise1/  start/ and end/ for the spotlight
exercise2/  start/ and end/ for the Upside Down
shared/     Product screen and prepared effects
card/       Product UI
player/     Video hosts
examples/   Finished video routes and Kotlin comparison
```

Five finished routes are also available through `exampleToRun` in `MainActivity.kt` or an intent: `NoVideoAgsl`, `SurfaceViewAgsl`, `TextureViewAgsl`, `SurfaceViewPipeline` and `SurfaceViewRawGl`. These support the video-composition discussion after the live exercises. WholeSceneGl is outside this sample.

```sh
./gradlew :app:installDebug
adb shell am start -W -S -n dev.maxkach.shaderworkshop/.MainActivity --es example SurfaceViewPipeline --ez effect true
```

## Check your changes

```sh
./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
ANDROID_SERIAL=your-device ./gradlew :app:connectedDebugAndroidTest
python3 -m unittest discover -s scripts -p 'test_*.py'
python3 scripts/verify-copy-recovery.py
python3 scripts/verify-uncomment-recovery.py --device your-device
```

The recovery tools build disposable copies and leave your exercise files unchanged. The uncomment check compiles each Kotlin spotlight step in sequence, then builds both completed exercises and optionally compares their screens on a device. Set `ANDROID_HOME` to your SDK directory for those copies.

Presenter: [Maxim Kachinkin](https://maxkach.carrd.co/). See [NOTICE.md](NOTICE.md) for attribution and asset status. No project-wide open-source license has been selected.
