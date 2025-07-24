package com.kosa.selp.shared.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.gift.model.Gift

@Composable
fun GiftGrid(
    gifts: List<Gift>,
    visibleItems: List<Boolean>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(gifts) { index, gift ->
            val isVisible = visibleItems.getOrNull(index) == true
            AnimatedGiftCard(gift = gift, visible = isVisible)
        }
    }
}