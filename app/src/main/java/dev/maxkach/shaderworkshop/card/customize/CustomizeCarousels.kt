package dev.maxkach.shaderworkshop.card.customize

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.maxkach.shaderworkshop.card.ChipSlot
import dev.maxkach.shaderworkshop.card.DrinkitColor
import dev.maxkach.shaderworkshop.card.DrinkitFont
import dev.maxkach.shaderworkshop.card.GroupChip
import dev.maxkach.shaderworkshop.card.chipSlots
import dev.maxkach.shaderworkshop.card.groupChip
import dev.maxkach.shaderworkshop.card.scaleClickable

@Composable
internal fun CustomizeCarousels(
  state: CustomizeState,
  modifier: Modifier = Modifier,
  chips: LazyListState = rememberLazyListState(),
  optionRows: MutableMap<String, LazyListState>? = null,
) {
  val expanded = if (state.isOpened) state.groups.getOrNull(state.currentPage) else null

  Column(modifier = modifier.fillMaxWidth()) {
    AnimatedContent(
        targetState = expanded,
        contentKey = { it?.id },
        transitionSpec = {
          (slideInVertically { it / 2 } + fadeIn()) togetherWith
              (slideOutVertically { it / 2 } + fadeOut()) using SizeTransform(clip = false)
        },
        label = "optionCards",
    ) { group ->
      if (group != null) {
        val rowState = rememberLazyListState()

        if (optionRows != null) DisposableEffect(group.id, rowState) {
          optionRows[group.id] = rowState
          onDispose { if (optionRows[group.id] === rowState) optionRows.remove(group.id) }
        }
        LazyRow(
            state = rowState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = ChipSpacing)
                .height(OptionCardHeight),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(ChipSpacing),
        ) {
          items(group.items, key = { it.id }) { item ->
            OptionCard(
                item = item,
                isSelected = state.isSelected(group.id, item.id),
                onClick = { state.toggle(group.id, item.id) },
            )
          }
        }
      }
    }

    LazyRow(
        state = chips,
        modifier = Modifier
            .fillMaxWidth()
            .height(GroupChipHeight),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(ChipSpacing),
    ) {
      itemsIndexed(state.groups, key = { _, group -> group.id }) { index, group ->
        GroupChipView(
            chip = groupChip(group, state),
            isExpanded = expanded?.id == group.id,
            onClick = { state.onTabClick(index) },
        )
      }
    }
  }
}

@Composable
private fun GroupChipView(chip: GroupChip, isExpanded: Boolean, onClick: () -> Unit) {
  val scale by animateFloatAsState(if (isExpanded) 1.03f else 1f, label = "chipScale")
  val borderAlpha by animateFloatAsState(if (isExpanded) 0.9f else 0f, label = "chipBorder")
  Column(
      modifier = Modifier
          .scaleClickable(onClick)
          .graphicsLayer {
            scaleX = scale
            scaleY = scale
          }
          .size(width = 104.dp, height = GroupChipHeight)
          .clip(ChipShape)
          .background(DrinkitColor.BlurOverlay)
          .border(1.5.dp, Color.White.copy(alpha = borderAlpha), ChipShape),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
      ChipCover(chip.images)
    }
    Text(
        text = chip.label,
        style = DrinkitFont.TextS,
        color = Color.White,
        textAlign = TextAlign.Center,
        maxLines = 2,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 6.dp),
    )
    Text(
        text = chip.price ?: " ",
        style = DrinkitFont.TextXS,
        color = DrinkitColor.White60,
        modifier = Modifier.padding(top = 1.dp),
    )
  }
}

@Composable
private fun ChipCover(images: List<Int>) {
  when (images.size) {
    0 -> Box(
        modifier = Modifier
            .size(36.dp)
            .background(Color.Black.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
      Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
    }

    1 -> Image(painterResource(images[0]), contentDescription = null, modifier = Modifier.size(40.dp))

    else -> Column {
      chipSlots(images).chunked(2).forEach { row ->
        Row {
          row.forEach { slot ->
            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
              when (slot) {
                is ChipSlot.Image -> Image(painterResource(slot.image), contentDescription = null, modifier = Modifier.size(24.dp))
                is ChipSlot.Counter -> Box(
                    modifier = Modifier
                        .size(width = 24.dp, height = 18.dp)
                        .background(Color.White, RoundedCornerShape(9.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                  Text(text = "+${slot.count}", style = DrinkitFont.TextXS, color = Color.Black)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun OptionCard(item: CustomizeItem, isSelected: Boolean, onClick: () -> Unit) {
  val background by animateColorAsState(
      if (isSelected) Color.White else DrinkitColor.BlurOverlay,
      label = "optionBackground",
  )
  val foreground = if (isSelected) Color.Black else Color.White
  Column(
      modifier = Modifier
          .scaleClickable(onClick)
          .size(width = 112.dp, height = OptionCardHeight)
          .clip(ChipShape)
          .background(background)
          .padding(horizontal = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
        painter = painterResource(item.image),
        contentDescription = null,
        modifier = Modifier
            .padding(top = 6.dp, bottom = 4.dp)
            .size(64.dp),
    )
    Text(
        text = item.name,
        style = DrinkitFont.TextS,
        color = foreground,
        textAlign = TextAlign.Center,
        maxLines = 2,
    )
    Text(
        text = item.price ?: " ",
        style = DrinkitFont.TextXS,
        color = if (isSelected) DrinkitColor.Black30 else DrinkitColor.White60,
        maxLines = 1,
        modifier = Modifier.padding(top = 4.dp),
    )
    Spacer(Modifier.weight(1f))
    Icon(
        imageVector = if (isSelected) Icons.Filled.Check else Icons.Filled.Add,
        contentDescription = if (isSelected) "Selected" else "Add",
        tint = foreground,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .size(14.dp),
    )
  }
}

private val ChipSpacing = 8.dp
private val GroupChipHeight = 124.dp
private val OptionCardHeight = 164.dp
private val ChipShape = RoundedCornerShape(20.dp)
