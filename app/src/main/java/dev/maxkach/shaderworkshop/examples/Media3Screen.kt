package dev.maxkach.shaderworkshop.examples

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import kotlinx.collections.immutable.persistentListOf
import dev.maxkach.shaderworkshop.SampleScaffold
import dev.maxkach.shaderworkshop.card.DemoProductCard
import dev.maxkach.shaderworkshop.shared.effect.rememberUpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.end.glitch.upsideDownGlitch
import dev.maxkach.shaderworkshop.exercise2.end.grading.upsideDownGrading
import dev.maxkach.shaderworkshop.exercise2.end.vines.upsideDownVines
import dev.maxkach.shaderworkshop.exercise2.end.snow.upsideDownSnow
import dev.maxkach.shaderworkshop.shared.effect.gl.UpsideDownGlEffect
import dev.maxkach.shaderworkshop.player.SurfaceViewVideo
import dev.maxkach.shaderworkshop.player.rememberDemoPlayer

@OptIn(UnstableApi::class)
@Composable
fun Media3Screen(startActive: Boolean = false, shadeUi: Boolean = true, onBack: () -> Unit = {}) {
    val effect = rememberUpsideDownEffectState(initiallyActive = startActive)
    val videoEffect = remember(effect) { UpsideDownGlEffect(effect) }
    val player = rememberDemoPlayer(effects = persistentListOf(videoEffect))
    SampleScaffold(player, onBack) {
        val overlays = if (shadeUi) Modifier.upsideDownSnow(effect).upsideDownVines(effect) else Modifier
        Box(Modifier.fillMaxSize().then(overlays)) {
            DemoProductCard(
                chromeModifier = if (shadeUi) Modifier.upsideDownGlitch(effect).upsideDownGrading(effect) else Modifier,
                isUpsideDownActive = effect.isActive,
                onUpsideDownClick = effect::toggle,
                background = { SurfaceViewVideo(player, Modifier.matchParentSize()) },
            )
        }
    }
}
