package com.kosa.selp.features.gift.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.gift.viewModel.AgeGroupGiftViewModel
import com.kosa.selp.features.home.presentation.screen.GiftItem
import com.kosa.selp.shared.composable.gift.GiftCardGrid
import com.kosa.selp.shared.theme.AppColor

@Composable
fun AgeGroupGiftScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AgeGroupGiftViewModel = hiltViewModel()
) {
    val ageGroups = listOf("10s", "20s", "30s", "40s", "50s")
    val selected = viewModel.selectedAgeGroup
    val gifts by viewModel.gifts.collectAsState()

    val converted = gifts.map { gift ->
        GiftItem(
            id = gift.id,
            title = gift.name,
            imageUrl = gift.imageUrl,
            price = gift.price,
            detailPath = gift.detailPath,
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.white)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "연령대별 추천 선물",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ageGroups) { ageGroup ->
                    val isSelected = selected == ageGroup
                    Button(
                        onClick = { viewModel.selectAgeGroup(ageGroup) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) AppColor.primary else AppColor.white,
                            contentColor = if (isSelected) AppColor.white else AppColor.textPrimary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = if (!isSelected) {
                            BorderStroke(1.dp, AppColor.divider)
                        } else {
                            null
                        }
                    ) {
                        Text(ageGroup.toKoreanLabel())
                    }
                }
            }
        }

        item {
            Text("이런 선물은 어때요?", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            GiftCardGrid(items = converted, navController = navController)
        }

    }
}

fun String.toKoreanLabel(): String {
    return when (this) {
        "10s" -> "10대"
        "20s" -> "20대"
        "30s" -> "30대"
        "40s" -> "40대"
        "50s" -> "50대"
        else -> this
    }
}
