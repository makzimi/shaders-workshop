package dev.maxkach.shaderworkshop.card

import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import dev.maxkach.shaderworkshop.card.customize.CustomizeGroup
import dev.maxkach.shaderworkshop.card.customize.CustomizeItem
import dev.maxkach.shaderworkshop.card.customize.CustomizeState

class ProductModelsTest {
  private val milk = CustomizeGroup(
      id = "milk", name = "Milk", singleChoice = true,
      items = persistentListOf(
          CustomizeItem("cow", "Cow milk", 1, null),
          CustomizeItem("oat", "Oat milk", 2, "+ 60 ₽"),
      ),
  )
  private val syrups = CustomizeGroup(
      id = "syrups", name = "Syrups", singleChoice = false,
      items = persistentListOf(
          CustomizeItem("caramel", "Caramel", 3, "+ 45 ₽"),
          CustomizeItem("vanilla", "Vanilla", 4, "+ 45 ₽"),
          CustomizeItem("mint", "Mint", 5, null),
      ),
  )

  @Test fun cupSizesMatchTheIosCard() {
    assertEquals(listOf("S", "M", "L"), CupSize.entries.map { it.label })
    assertEquals(listOf(250, 350, 450), CupSize.entries.map { it.milliliters })
    assertEquals(listOf(240, 285, 330), CupSize.entries.map { it.price })
    assertEquals(listOf(0.72, 1.0, 1.28), CupSize.entries.map { it.nutritionFactor })
  }

  @Test fun priceLabelsParseToRubles() {
    assertEquals(30, rublesOf("+ 30 ₽"))
    assertEquals(125, rublesOf("+125 ₽"))
    assertEquals(0, rublesOf(null))
    assertEquals(0, rublesOf("free"))
  }

  @Test fun totalPriceAddsPaidSelectionsToTheSizePrice() {
    val state = CustomizeState(persistentListOf(milk, syrups))
    assertEquals(285, totalPrice(CupSize.M, state))
    state.toggle("milk", "oat")
    state.toggle("syrups", "caramel")
    state.toggle("syrups", "mint")
    assertEquals(330 + 60 + 45, totalPrice(CupSize.L, state))
  }

  @Test fun nutritionScalesWithTheCupSize() {
    val s = nutritionFacts(CupSize.S)
    assertEquals(listOf("caffeine", "energy", "protein", "fat", "carbs"), s.map { it.label })
    assertEquals(listOf("85 mg", "114 kcal", "5.5 g", "6.0 g", "8.8 g"), s.map { it.value })
    assertEquals(listOf("118 mg", "158 kcal", "7.7 g", "8.4 g", "12.2 g"), nutritionFacts(CupSize.M).map { it.value })
  }

  @Test fun singleChoiceChipShowsTheSelectedItemWithoutAPrice() {
    val state = CustomizeState(persistentListOf(milk, syrups))
    state.toggle("milk", "oat")
    val chip = groupChip(milk, state)
    assertEquals("Oat milk", chip.label)
    assertEquals(listOf(2), chip.images)
    assertNull(chip.price)
  }

  @Test fun multipleChoiceChipSumsItsSelection() {
    val state = CustomizeState(persistentListOf(milk, syrups))
    assertEquals(GroupChip("Syrups", emptyList(), null), groupChip(syrups, state))
    state.toggle("syrups", "caramel")
    state.toggle("syrups", "vanilla")
    assertEquals(GroupChip("Syrups", listOf(3, 4), "90 ₽"), groupChip(syrups, state))
  }

  @Test fun chipCoverShowsFourSlotsWithACounterBeyondFour() {
    assertEquals(listOf(ChipSlot.Image(1)), chipSlots(listOf(1)))
    assertEquals((1..4).map { ChipSlot.Image(it) }, chipSlots(listOf(1, 2, 3, 4)))
    assertEquals(
        listOf(ChipSlot.Image(1), ChipSlot.Counter(3), ChipSlot.Image(3), ChipSlot.Image(4)),
        chipSlots(listOf(1, 2, 3, 4, 5, 6)),
    )
  }

  @Test fun sizeFromDragPositionSplitsTheSelectorIntoThirds() {
    assertEquals(CupSize.S, sizeAt(x = 10f, width = 300f))
    assertEquals(CupSize.M, sizeAt(x = 150f, width = 300f))
    assertEquals(CupSize.L, sizeAt(x = 290f, width = 300f))
    assertEquals(CupSize.S, sizeAt(x = -40f, width = 300f))
    assertEquals(CupSize.L, sizeAt(x = 400f, width = 300f))
  }
}
