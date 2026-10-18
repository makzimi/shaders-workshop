package dev.maxkach.shaderworkshop.card

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import dev.maxkach.shaderworkshop.R

internal object DrinkitColor {
  val ButtonPrimary = Color(0xFF334CDB)
  val White60 = Color.White.copy(alpha = 0.6f)
  val White10 = Color.White.copy(alpha = 0.1f)
  val Black30 = Color.Black.copy(alpha = 0.3f)

  val BlurOverlay = Color.Black.copy(alpha = 0.4f)
}

private val SuisseIntl = FontFamily(Font(R.font.suisse_intl_regular))
private val SuisseIntlTextL = FontFamily(Font(R.font.suisse_intl_text_l))

internal object DrinkitFont {
  val Lead3 = TextStyle(fontFamily = SuisseIntl, fontSize = 20.sp)
  val TextL = TextStyle(fontFamily = SuisseIntlTextL, fontSize = 16.sp)
  val TextS = TextStyle(fontFamily = SuisseIntl, fontSize = 12.sp)
  val TextXS = TextStyle(fontFamily = SuisseIntl, fontSize = 10.sp)
  val NutritionValue = TextStyle(fontFamily = SuisseIntl, fontSize = 15.sp)
}

val LocalProductBack = staticCompositionLocalOf<() -> Unit> { {} }

@Composable
internal fun Modifier.scaleClickable(onClick: () -> Unit): Modifier {
  val interaction = remember { MutableInteractionSource() }
  val pressed by interaction.collectIsPressedAsState()
  val scale by animateFloatAsState(
      targetValue = if (pressed) 0.95f else 1f,
      animationSpec = spring(stiffness = Spring.StiffnessMedium),
      label = "pressScale",
  )
  return graphicsLayer {
    scaleX = scale
    scaleY = scale
  }.clickable(interactionSource = interaction, indication = null, onClick = onClick)
}
