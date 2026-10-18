package dev.maxkach.shaderworkshop.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.SampleScaffold
import dev.maxkach.shaderworkshop.card.DemoProductCard
import dev.maxkach.shaderworkshop.shared.effect.rememberUpsideDownEffectState
import dev.maxkach.shaderworkshop.exercise2.end.agsl.upsideDownAgsl
import dev.maxkach.shaderworkshop.shared.effect.mesh.upsideDownRouteVines
import dev.maxkach.shaderworkshop.shared.effect.snow.upsideDownSnow
import dev.maxkach.shaderworkshop.player.GlVideoSurface
import dev.maxkach.shaderworkshop.player.rememberDemoPlayer

@Composable
fun GlRendererScreen(startActive: Boolean = false, continuous: Boolean = true, onBack: () -> Unit = {}) {
    val effect = rememberUpsideDownEffectState(initiallyActive = startActive)
    val player = rememberDemoPlayer()
    SampleScaffold(player, onBack) {
        Box(Modifier.fillMaxSize().upsideDownSnow(effect).upsideDownRouteVines(effect)) {
            DemoProductCard(
                chromeModifier = Modifier.upsideDownAgsl(effect, particleCount = 0, drawVines = false),
                isUpsideDownActive = effect.isActive,
                onUpsideDownClick = effect::toggle,
                background = { GlVideoSurface(player, effect, Modifier.matchParentSize(), continuous) },
            )
        }
    }
}
