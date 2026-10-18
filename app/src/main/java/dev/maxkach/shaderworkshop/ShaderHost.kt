package dev.maxkach.shaderworkshop

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import ru.redbyte.redbytefx.FxEffect
import ru.redbyte.redbytefx.FxParam

@Composable
fun Modifier.agslEffect(source: String, timeSeconds: Float): Modifier {
    val shader = remember(source) { RuntimeShader(source) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    return onSizeChanged { size = it }.graphicsLayer {

        clip = true
        if (size.width > 0 && size.height > 0) {
            shader.setFloatUniform("resolution", size.width.toFloat(), size.height.toFloat())
            shader.setFloatUniform("time", timeSeconds)

            renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "content").asComposeRenderEffect()
        }
    }
}

data class KotlinExample(val effect: FxEffect, val time: FxParam.Float)

@Composable
fun Modifier.kotlinEffect(example: KotlinExample, timeSeconds: Float): Modifier {

    val instance = remember(example) { example.effect.newInstance() }
    var size by remember { mutableStateOf(IntSize.Zero) }
    return onSizeChanged { size = it }.graphicsLayer {
        clip = true
        if (size.width > 0 && size.height > 0) {
            instance.setResolution(size.width.toFloat(), size.height.toFloat())
            instance.setFloat(example.time, timeSeconds)
            renderEffect = instance.renderEffect().asComposeRenderEffect()
        }
    }
}
