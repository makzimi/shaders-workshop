package dev.maxkach.shaderworkshop.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import dev.maxkach.shaderworkshop.R

@Composable
internal fun ProductTopBar(
  title: String,
  isUpsideDownOn: Boolean,
  onUpsideDownClick: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
      modifier = modifier
          .fillMaxWidth()
          .height(64.dp)
          .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Box(
        modifier = Modifier
            .scaleClickable(onBack)
            .size(44.dp)
            .clip(CircleShape)
            .background(DrinkitColor.BlurOverlay),
        contentAlignment = Alignment.Center,
    ) {

      Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
          contentDescription = "Back",
          tint = Color.White,
          modifier = Modifier
              .padding(start = 6.dp)
              .size(20.dp),
      )
    }

    Text(
        text = title,
        style = DrinkitFont.Lead3,
        color = Color.White,
        textAlign = TextAlign.Center,
        maxLines = 2,
        modifier = Modifier.weight(1f),
    )

    val rotation by animateFloatAsState(
        targetValue = if (isUpsideDownOn) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = 322f),
        label = "upsideDownRotation",
    )
    Image(
        painter = painterResource(R.drawable.ic_the_upside_down),
        contentDescription = if (isUpsideDownOn) "Back to normal" else "Upside Down",
        modifier = Modifier
            .scaleClickable(onUpsideDownClick)
            .size(44.dp)
            .graphicsLayer { rotationZ = rotation },
    )
  }
}

@Composable
internal fun NutritionFactsRow(facts: List<NutritionFact>, modifier: Modifier = Modifier) {
  var appeared by remember { mutableStateOf(false) }
  LaunchedEffect(Unit) {
    delay(100)
    appeared = true
  }

  Row(
      modifier = modifier
          .fillMaxWidth()
          .height(42.dp)
          .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    facts.forEachIndexed { index, fact ->
      val progress by animateFloatAsState(
          targetValue = if (appeared) 1f else 0f,
          animationSpec = tween(durationMillis = 300, delayMillis = index * 50),
          label = "nutritionAppear",
      )
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(2.dp),
          modifier = Modifier.graphicsLayer {
            translationX = (1f - progress) * 50.dp.toPx()
            alpha = progress
          },
      ) {
        AnimatedContent(
            targetState = fact.value,
            transitionSpec = {
              (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
            },
            label = "nutritionValue",
        ) { value ->
          Text(text = value, style = DrinkitFont.NutritionValue, color = Color.White, maxLines = 1)
        }
        Text(text = fact.label, style = DrinkitFont.TextS, color = DrinkitColor.White60, maxLines = 1)
      }
    }
  }
}

@Composable
internal fun IngredientsPill(modifier: Modifier = Modifier) {
  Row(
      modifier = modifier
          .height(32.dp)
          .clip(CircleShape)
          .background(DrinkitColor.BlurOverlay)
          .padding(start = 12.dp, end = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Text(text = "what's inside", style = DrinkitFont.TextS, color = Color.White)
    Icon(
        imageVector = Icons.Filled.KeyboardArrowDown,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(16.dp),
    )
  }
}
