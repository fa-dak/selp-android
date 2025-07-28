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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.survey.presentation.state.SurveyEvent
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.theme.AppColor

data class AnniversaryOption(
    val id: String,
    val displayName: String
)

val allAnniversaries = listOf(
    AnniversaryOption("생일", "생일"),
    AnniversaryOption("결혼기념일", "결혼기념일"),
    AnniversaryOption("입학/졸업", "입학/졸업"),
    AnniversaryOption("취업/승진", "취업/승진"),
    AnniversaryOption("집들이", "집들이"),
    AnniversaryOption("출산/돌잔치", "출산/돌잔치"),
    AnniversaryOption("명절", "명절"),
    AnniversaryOption("기타", "기타")
)

@Composable
fun AnniversaryFunnel(
    viewModel: SurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val selected = remember(state.anniversary) { mutableStateOf(state.anniversary) }

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
            allAnniversaries.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { anniversary ->
                        val isSelected = selected.value == anniversary.id

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
                                selected.value = anniversary.id
                                viewModel.onEvent(SurveyEvent.AnniversarySelected(anniversary.id))
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
                                text = anniversary.displayName,
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
            enabled = selected.value != null,
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

