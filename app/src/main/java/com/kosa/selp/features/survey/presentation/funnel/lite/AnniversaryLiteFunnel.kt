package com.kosa.selp.features.survey.presentation.funnel.lite

import android.util.Log
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
import com.kosa.selp.features.survey.model.AnniversaryType
import com.kosa.selp.features.survey.presentation.state.LiteSurveyEvent
import com.kosa.selp.features.survey.presentation.viewModel.LiteSurveyViewModel
import com.kosa.selp.shared.theme.AppColor

@Composable
fun AnniversaryLiteFunnel(
    viewModel: LiteSurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "어떤 기념일을 위한 선물인가요?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColor.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AnniversaryType.entries.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { anniversary ->
                        val isSelected = state.anniversary == anniversary.code

                        Log.d(
                            "AnniversaryLiteFunnel",
                            "현재 선택된 코드: ${state.anniversary}, 비교 대상: ${anniversary.code}"
                        )

                        val backgroundColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary.copy(alpha = 0.1f) else AppColor.white,
                            label = "AnnivBackground"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary else AppColor.divider,
                            label = "AnnivBorder"
                        )
                        val contentColor by animateColorAsState(
                            targetValue = if (isSelected) AppColor.primary else AppColor.textPrimary,
                            label = "AnnivText"
                        )

                        OutlinedButton(
                            onClick = {
                                viewModel.onEvent(LiteSurveyEvent.AnniversarySelected(anniversary.code))
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
                                text = anniversary.label,
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
            enabled = state.anniversary != null,
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
