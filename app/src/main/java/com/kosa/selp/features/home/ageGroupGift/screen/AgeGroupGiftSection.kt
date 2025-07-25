package com.kosa.selp.features.home.ageGroupGift.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kosa.selp.features.home.ageGroupGift.viewModel.AgeGroupGiftViewModel
import com.kosa.selp.shared.theme.AppColor
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import com.kosa.selp.features.home.presentation.screen.GiftCard
import com.kosa.selp.features.home.presentation.screen.GiftItem

@Composable
fun AgeGroupGiftSection() {
    val viewModel: AgeGroupGiftViewModel = viewModel()
    val ageGroups = listOf("10대", "20대", "30대", "40대", "50대")
    val selected = viewModel.selectedAgeGroup
    val gifts = viewModel.gifts

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("연령대별 추천 선물", fontWeight = FontWeight.SemiBold)

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

        Spacer(Modifier.height(8.dp))

        gifts.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowItems.forEach { gift ->
                    GiftCard(
                        gift = GiftItem(gift.title, gift.imageUrl, gift.price),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}