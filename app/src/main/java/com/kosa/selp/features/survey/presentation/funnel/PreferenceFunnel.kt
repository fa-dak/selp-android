package com.kosa.selp.features.survey.presentation.funnel

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.survey.viewModel.SurveyEvent
import com.kosa.selp.features.survey.viewModel.SurveyViewModel
import com.kosa.selp.shared.theme.AppColor

@Composable
fun PreferenceFunnel(
    viewModel: SurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val allPreferences = listOf("패션", "테크", "푸드", "취미", "웰니스", "여가", "여행", "건강")

    val selected = remember(state.preferences) { state.preferences.toMutableStateList() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "해당 분의 취향에 맞는 항목을 골라주세요", style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ), color = AppColor.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            allPreferences.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { pref ->
                        val isSelected = selected.contains(pref)

                        val backgroundColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary.copy(alpha = 0.1f) else AppColor.white,
                            label = "PrefBackground"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary else AppColor.divider,
                            label = "PrefBorder"
                        )
                        val contentColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary else AppColor.textPrimary,
                            label = "PrefText"
                        )

                        OutlinedButton(
                            onClick = {
                                if (isSelected) selected.remove(pref)
                                else selected.add(pref)
                                viewModel.onEvent(SurveyEvent.PreferencesSelected(selected.toList()))
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
                                text = pref,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                        }
                    }
                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f)) // 홀수 개 보정
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
