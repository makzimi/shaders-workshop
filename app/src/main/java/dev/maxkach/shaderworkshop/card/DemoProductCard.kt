package dev.maxkach.shaderworkshop.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.maxkach.shaderworkshop.card.customize.CappuccinoCustomizeGroups
import dev.maxkach.shaderworkshop.card.customize.CustomizeCarousels
import dev.maxkach.shaderworkshop.card.customize.CustomizeState
import dev.maxkach.shaderworkshop.teaching.captureAwareNavigationBarsPadding
import dev.maxkach.shaderworkshop.teaching.captureAwareStatusBarsPadding

@Composable
fun DemoProductCard(
  modifier: Modifier = Modifier,
  title: String = "Cappuccino",
  chromeModifier: Modifier = Modifier,
  isUpsideDownActive: Boolean = false,
  onUpsideDownClick: () -> Unit = {},
  background: @Composable BoxScope.() -> Unit = {},
) {
  val customizeState = remember { CustomizeState(CappuccinoCustomizeGroups) }
  var size by rememberSaveable { mutableStateOf(CupSize.M) }
  val chips = rememberLazyListState()
  val optionRows = remember { mutableMapOf<String, LazyListState>() }

  Box(modifier = modifier.fillMaxSize()) {
    background()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Color.Black.copy(alpha = 0.7f),
                    0.35f to Color.Transparent,
                    0.55f to Color.Transparent,
                    1f to Color.Black.copy(alpha = 0.6f),
                )
            ),
    )

    Column(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .then(chromeModifier)
            .fillMaxWidth()
            .captureAwareStatusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      ProductTopBar(
          title = title,
          isUpsideDownOn = isUpsideDownActive,
          onUpsideDownClick = onUpsideDownClick,
          onBack = LocalProductBack.current,
      )
      NutritionFactsRow(facts = nutritionFacts(size))
      Spacer(Modifier.height(12.dp))
      IngredientsPill()
    }

    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .then(chromeModifier)
            .fillMaxWidth()
            .captureAwareNavigationBarsPadding(),
    ) {
      CustomizeCarousels(state = customizeState, chips = chips, optionRows = optionRows)
      ProductFloatingFooter(
          size = size,
          onSizeChange = { size = it },
          price = totalPrice(size, customizeState),
          modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
      )
    }
  }
}
