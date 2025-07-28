package com.kosa.selp.features.survey.presentation.funnel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.survey.presentation.state.SurveyEvent
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.theme.AppColor


@Composable
fun BudgetFunnel(
    viewModel: SurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    var budget by remember { mutableStateOf(state.budget?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "예산을 설정해주세요",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColor.textPrimary,
            modifier = Modifier.padding(top = 32.dp)
        )

        Text(
            text = if (budget.isEmpty()) "금액을 입력하세요" else "${formatWithCommas(budget)}원",
            style = MaterialTheme.typography.titleLarge.copy(
                color = if (budget.isEmpty()) AppColor.textHint else AppColor.textPrimary,
                textAlign = TextAlign.End
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = AppColor.primary
        )

        Spacer(modifier = Modifier.weight(1f))

        Column {
            BudgetPad(
                value = budget,
                onValueChange = { budget = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onEvent(SurveyEvent.BudgetSelected(budget.toInt()))
                    onNext()
                },
                enabled = (budget.toIntOrNull() ?: 0) >= 10_000,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.primary,
                    contentColor = AppColor.white
                )
            ) {
                Text("다음", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun BudgetPad(
    value: String,
    onValueChange: (String) -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("00", "0", "X")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEach { label ->
                    BudgetPadButton(
                        label = label,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        val raw = when (label) {
                            "X" -> value.dropLast(1)
                            else -> value + label
                        }
                        val new = raw.filter { it.isDigit() }
                            .toLongOrNull()
                            ?.coerceAtMost(3_000_000)
                            ?.toString()
                            ?: ""
                        onValueChange(new)
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetPadButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColor.white,
            contentColor = AppColor.textPrimary
        )
    ) {
        Text(label, style = MaterialTheme.typography.titleLarge)
    }
}

fun formatWithCommas(number: String): String {
    return number.toLongOrNull()?.let {
        "%,d".format(it)
    } ?: number
}
