package com.kosa.selp.features.gift.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.data.response.GiftItemDto
import com.kosa.selp.features.home.presentation.screen.GiftItem
import com.kosa.selp.shared.composable.gift.GiftCardGrid

@Composable
fun GiftBundleContent(
    giftBundle: GiftBundleDetailResponseDto,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            GiftCardGrid(
                items = giftBundle.giftList.map { it.toGiftItem() },
                navController = navController,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

fun GiftItemDto.toGiftItem(): GiftItem {
    return GiftItem(
        id = this.id,
        title = this.name,
        price = this.price,
        imageUrl = this.imageUrl
    )
}
