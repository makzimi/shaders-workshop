package dev.maxkach.shaderworkshop.card

import androidx.annotation.DrawableRes
import java.util.Locale
import dev.maxkach.shaderworkshop.card.customize.CustomizeGroup
import dev.maxkach.shaderworkshop.card.customize.CustomizeState

enum class CupSize(val label: String, val milliliters: Int, val price: Int, val nutritionFactor: Double) {
  S("S", 250, 240, 0.72),
  M("M", 350, 285, 1.0),
  L("L", 450, 330, 1.28),
}

fun rublesOf(priceLabel: String?): Int =
    priceLabel?.filter(Char::isDigit)?.toIntOrNull() ?: 0

fun totalPrice(size: CupSize, state: CustomizeState): Int =
    size.price + state.groups.sumOf { group ->
      group.items.filter { state.isSelected(group.id, it.id) }.sumOf { rublesOf(it.price) }
    }

data class NutritionFact(val value: String, val label: String)

fun nutritionFacts(size: CupSize): List<NutritionFact> {
  val f = size.nutritionFactor
  return listOf(
      NutritionFact(format("%.0f mg", 118.0 * f), "caffeine"),
      NutritionFact(format("%.0f kcal", 158.0 * f), "energy"),
      NutritionFact(format("%.1f g", 7.7 * f), "protein"),
      NutritionFact(format("%.1f g", 8.4 * f), "fat"),
      NutritionFact(format("%.1f g", 12.2 * f), "carbs"),
  )
}

private fun format(pattern: String, value: Double) = String.format(Locale.US, pattern, value)

data class GroupChip(val label: String, @DrawableRes val images: List<Int>, val price: String?)

fun groupChip(group: CustomizeGroup, state: CustomizeState): GroupChip {
  val picked = group.items.filter { state.isSelected(group.id, it.id) }
  return if (group.singleChoice) {
    val current = picked.firstOrNull()
    GroupChip(current?.name ?: group.name, listOfNotNull(current?.image), null)
  } else {
    val total = picked.sumOf { rublesOf(it.price) }
    GroupChip(group.name, picked.map { it.image }, if (total > 0) "$total ₽" else null)
  }
}

sealed interface ChipSlot {
  data class Image(@DrawableRes val image: Int) : ChipSlot
  data class Counter(val count: Int) : ChipSlot
}

fun chipSlots(images: List<Int>): List<ChipSlot> =
    if (images.size <= 4) {
      images.map(ChipSlot::Image)
    } else {
      listOf(ChipSlot.Image(images[0]), ChipSlot.Counter(images.size - 3), ChipSlot.Image(images[2]), ChipSlot.Image(images[3]))
    }

fun sizeAt(x: Float, width: Float): CupSize = when {
  x <= width * 0.333f -> CupSize.S
  x <= width * 0.666f -> CupSize.M
  else -> CupSize.L
}
