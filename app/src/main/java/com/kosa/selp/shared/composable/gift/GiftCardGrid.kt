package com.kosa.selp.shared.composable.gift

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.home.presentation.screen.GiftItem

@Composable
fun GiftCardGrid(
    items: List<GiftItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items.chunked(2).forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { gift ->
                        GiftCard(
                            gift = gift,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigate("giftDetail/${gift.id}")
                                }
                        )
                    }

                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}