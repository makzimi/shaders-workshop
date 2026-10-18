package dev.maxkach.shaderworkshop.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.maxkach.shaderworkshop.exercise2.end.workshopEffect
import dev.maxkach.shaderworkshop.SampleScaffold
import dev.maxkach.shaderworkshop.card.DemoProductCard
import dev.maxkach.shaderworkshop.shared.effect.rememberUpsideDownEffectState
import dev.maxkach.shaderworkshop.player.TextureViewVideo
import dev.maxkach.shaderworkshop.player.rememberDemoPlayer

@Composable
fun TextureViewScreen(startActive: Boolean = false, onBack: () -> Unit = {}) {
    val effect = rememberUpsideDownEffectState(initiallyActive = startActive)
    val player = rememberDemoPlayer()
    SampleScaffold(player, onBack) {
        Box(Modifier.fillMaxSize().workshopEffect(effect)) {
            DemoProductCard(
                isUpsideDownActive = effect.isActive,
                onUpsideDownClick = effect::toggle,
                background = { TextureViewVideo(player, Modifier.matchParentSize()) },
            )
        }
    }
}
