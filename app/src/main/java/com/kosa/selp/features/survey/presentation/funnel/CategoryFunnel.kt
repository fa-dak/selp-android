package com.kosa.selp.features.survey.presentation.funnel

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.survey.presentation.state.SurveyEvent
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.theme.AppColor

data class CategoryOption(
    val id: String,
    val displayName: String
)

val allCategories = listOf(
    CategoryOption("beauty", "뷰티"),
    CategoryOption("food", "푸드"),
    CategoryOption("living", "리빙"),
    CategoryOption("children", "아동"),
    CategoryOption("sports", "스포츠"),
    CategoryOption("fashion", "패션"),
    CategoryOption("flower", "플라워"),
    CategoryOption("desert", "디저트")
)

@Composable
fun CategoryFunnel(
    viewModel: SurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val selected = remember(state.categories) { state.categories.toMutableStateList() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "해당 분의 취향에 맞는 항목을 골라주세요",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColor.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            allCategories.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { category ->
                        val isSelected = selected.contains(category.id)

                        val backgroundColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary.copy(alpha = 0.1f) else AppColor.white,
                            label = "CategoryBackground"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary else AppColor.divider,
                            label = "CategoryBorder"
                        )
                        val contentColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary else AppColor.textPrimary,
                            label = "CategoryText"
                        )

                        OutlinedButton(
                            onClick = {
                                if (isSelected) selected.remove(category.id)
                                else selected.add(category.id)
                                viewModel.onEvent(SurveyEvent.CategoriesSelected(selected.toList()))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = backgroundColor,
                                contentColor = contentColor
                            ),
                            border = BorderStroke(1.dp, borderColor)
                        ) {
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                        }
                    }

                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onNext() },
            enabled = selected.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColor.primary,
                contentColor = AppColor.white
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("다음", style = MaterialTheme.typography.labelLarge)
        }
    }
}
