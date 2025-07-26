package com.kosa.selp.features.home.ageGroupGift.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kosa.selp.features.home.ageGroupGift.viewModel.AgeGroupGiftViewModel
import com.kosa.selp.features.home.presentation.screen.GiftItem
import com.kosa.selp.shared.composable.gift.GiftCardGrid
import com.kosa.selp.shared.theme.AppColor

@Composable
fun AgeGroupGiftSection(navController: NavController) {
    val viewModel: AgeGroupGiftViewModel = viewModel()
    val ageGroups = listOf("10대", "20대", "30대", "40대", "50대")
    val selected = viewModel.selectedAgeGroup
    val gifts = viewModel.gifts

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ageGroups) { ageGroup ->
                val isSelected = selected == ageGroup
                Button(
                    onClick = { viewModel.selectAgeGroup(ageGroup) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) AppColor.primary else AppColor.secondary
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(ageGroup, color = if (isSelected) Color.White else AppColor.textPrimary)
                }
            }
        }

        GiftCardGrid(
            items = gifts.map { gift ->
                GiftItem(gift.id, gift.title, gift.imageUrl, gift.price)
            },
            navController = navController
        )
    }
}