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

The recovery tools build disposable copies and leave your exercise files unchanged. Set `ANDROID_HOME` to your SDK directory for those copies.

Presenter: [Maxim Kachinkin](https://maxkach.carrd.co/). See [NOTICE.md](NOTICE.md) for attribution and asset status. No project-wide open-source license has been selected.
