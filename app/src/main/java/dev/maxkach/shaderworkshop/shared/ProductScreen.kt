package dev.maxkach.shaderworkshop.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import dev.maxkach.shaderworkshop.R
import dev.maxkach.shaderworkshop.SampleScaffold
import dev.maxkach.shaderworkshop.card.DemoProductCard
import dev.maxkach.shaderworkshop.shared.effect.rememberUpsideDownEffectState
import dev.maxkach.shaderworkshop.shared.effect.UpsideDownEffectState

@Composable
fun ProductScreen(
    startActive: Boolean = false,
    onBack: () -> Unit = {},
    shade: @Composable (UpsideDownEffectState) -> Modifier,
) {
    val effect = rememberUpsideDownEffectState(initiallyActive = startActive)
    SampleScaffold(onBack = onBack) {
        Box(Modifier.fillMaxSize().then(shade(effect))) {
            DemoProductCard(
                isUpsideDownActive = effect.isActive,
                onUpsideDownClick = effect::toggle,
                background = {
                    Image(painterResource(R.drawable.cappuccino_cover), null,
                        Modifier.matchParentSize(), contentScale = ContentScale.Crop)
                },
            )
        }
    }
}
