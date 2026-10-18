package dev.maxkach.shaderworkshop.card.customize

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

@Immutable
data class CustomizeItem(
  val id: String,
  val name: String,
  @DrawableRes val image: Int,
  val price: String?,
)

@Immutable
data class CustomizeGroup(
  val id: String,
  val name: String,
  val singleChoice: Boolean,
  val items: ImmutableList<CustomizeItem>,
)

@Stable
class CustomizeState(val groups: ImmutableList<CustomizeGroup>) {

  var currentPage: Int by mutableIntStateOf(
      groups.indexOfFirst { it.name == "Milk" }.coerceAtLeast(0)
  )
    private set

  var isOpened: Boolean by mutableStateOf(false)
    private set

  private var selection: Map<String, Set<String>> by mutableStateOf(defaultSelection(groups))

  fun selectedIn(groupId: String): Set<String> = selection[groupId].orEmpty()

  fun isSelected(groupId: String, itemId: String): Boolean = itemId in selectedIn(groupId)

  fun onTabClick(page: Int) {
    if (isOpened && page == currentPage) {
      isOpened = false
    } else {
      currentPage = page
      isOpened = true
    }
  }

  fun close() {
    isOpened = false
  }

  fun toggle(groupId: String, itemId: String) {
    val group = groups.firstOrNull { it.id == groupId } ?: return
    val current = selectedIn(groupId)

    val next = when {
      group.singleChoice -> setOf(itemId)
      itemId in current -> current - itemId
      else -> current + itemId
    }

    selection = selection + (groupId to next)
  }

  fun tabImages(group: CustomizeGroup): ImmutableList<Int> {
    val selected = selectedIn(group.id)
    return group.items
        .filter { it.id in selected }
        .map { it.image }
        .toImmutableList()
  }
}

private fun defaultSelection(groups: ImmutableList<CustomizeGroup>): Map<String, Set<String>> {
  return groups.associate { group ->
    val selected = if (group.singleChoice) {
      group.items.take(1).map { it.id }.toImmutableSet()
    } else {
      persistentListOf<String>().toImmutableSet()
    }
    group.id to selected
  }
}
