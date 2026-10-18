package dev.maxkach.shaderworkshop.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import kotlin.math.roundToInt

internal val FooterHeight = 80.dp

@Composable
internal fun ProductFloatingFooter(
  size: CupSize,
  onSizeChange: (CupSize) -> Unit,
  price: Int,
  modifier: Modifier = Modifier,
) {
  BoxWithConstraints(
      modifier = modifier
          .fillMaxWidth()
          .height(FooterHeight)
          .clip(CircleShape)
          .background(DrinkitColor.BlurOverlay),
  ) {
    val inner = maxWidth - 24.dp
    val selectorWidth = (FooterHeight - 24.dp) * SelectorAspect

    val buttonWidth = min(maxWidth / 2, inner - selectorWidth - 16.dp)
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      SizeSelector(
          selected = size,
          onSelected = onSizeChange,
          modifier = Modifier
              .fillMaxHeight()
              .width(selectorWidth),
      )
      Spacer(Modifier.weight(1f))
      AddToCartButton(
          price = price,
          modifier = Modifier
              .fillMaxHeight()
              .width(buttonWidth),
      )
    }
  }
}

@Composable
internal fun SizeSelector(
  selected: CupSize,
  onSelected: (CupSize) -> Unit,
  modifier: Modifier = Modifier,
) {
  var current by remember { mutableStateOf(selected) }
  LaunchedEffect(selected) { current = selected }
  var dragging by remember { mutableStateOf(false) }
  var overshootDp by remember { mutableFloatStateOf(0f) }
  val haptics = LocalHapticFeedback.current
  val commit by rememberUpdatedState(onSelected)

  BoxWithConstraints(
      modifier = modifier.pointerInput(Unit) {
        awaitEachGesture {
          fun follow(x: Float) {
            val next = sizeAt(x, size.width.toFloat())
            if (next != current) {
              current = next
              haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
            overshootDp = when {
              x <= 0f -> -8f
              x > size.width -> 8f
              else -> 0f
            }
          }
          val down = awaitFirstDown()
          dragging = true
          follow(down.position.x)
          while (true) {
            val change = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
            if (!change.pressed) break
            follow(change.position.x)
            change.consume()
          }
          dragging = false
          overshootDp = 0f
          commit(current)
        }
      },
  ) {
    val knob = maxHeight
    val span = maxWidth - knob

    Box(
        Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(DrinkitColor.White10),
    )
    CupSize.entries.forEachIndexed { index, cup ->
      Box(
          modifier = Modifier
              .size(knob)
              .offset(x = span * index / 2),
          contentAlignment = Alignment.Center,
      ) {
        Text(text = cup.label, style = DrinkitFont.TextL, color = Color.White)
      }
    }

    val knobX by animateFloatAsState(
        targetValue = span.value * current.ordinal / 2f + overshootDp,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 500f),
        label = "knobX",
    )
    val knobScale by animateFloatAsState(if (dragging) 1.1f else 1f, label = "knobScale")
    Box(
        modifier = Modifier
            .offset { IntOffset((knobX - 1f).dp.roundToPx(), (-1).dp.roundToPx()) }
            .size(knob + 2.dp)
            .graphicsLayer {
              scaleX = knobScale
              scaleY = knobScale
            }
            .shadow(elevation = 10.dp, shape = CircleShape, ambientColor = KnobShadow, spotColor = KnobShadow)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = current.label, style = DrinkitFont.Lead3, color = Color.Black)
        Text(text = "${current.milliliters} ml", style = DrinkitFont.TextXS, color = Color.Black)
      }
    }
  }
}

@Composable
internal fun AddToCartButton(price: Int, modifier: Modifier = Modifier) {
  Box(
      modifier = modifier
          .scaleClickable {}
          .clip(CircleShape)
          .background(DrinkitColor.ButtonPrimary)
          .padding(8.dp),
      contentAlignment = Alignment.Center,
  ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Icon(
          imageVector = Icons.Filled.Add,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(18.dp),
      )
      AnimatedContent(
          targetState = price,
          transitionSpec = {
            val up = targetState > initialState
            (slideInVertically { if (up) it else -it } + fadeIn()) togetherWith
                (slideOutVertically { if (up) -it else it } + fadeOut())
          },
          label = "price",
      ) { value ->
        Text(text = "$value ₽", style = DrinkitFont.Lead3, color = Color.White, maxLines = 1)
      }
    }
  }
}

private const val SelectorAspect = 2.6f
private val KnobShadow = Color.Black.copy(alpha = 0.15f)
