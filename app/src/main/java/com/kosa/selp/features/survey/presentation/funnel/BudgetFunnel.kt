package com.kosa.selp.features.survey.presentation.funnel

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.survey.viewModel.SurveyEvent
import com.kosa.selp.features.survey.viewModel.SurveyViewModel
import com.kosa.selp.shared.composable.input.BudgetInputField
import com.kosa.selp.shared.theme.AppColor


@Composable
fun BudgetFunnel(
    viewModel: SurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    var budget by remember { mutableStateOf(state.budget?.toString() ?: "") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
            }
            .padding(24.dp)
    ) {
        Text(
            text = "예산을 설정해주세요",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColor.textPrimary
        )

        Spacer(modifier = Modifier.height(48.dp))

        BudgetInputField(
            value = budget,
            onValueChange = { budget = it },
            placeholder = "입력"
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.onEvent(SurveyEvent.BudgetSelected(budget.replace(",", "").toInt()))
                onNext()
            },
            enabled = budget.isNotBlank() && budget.replace(",","").toInt() >= 10000,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColor.primary, contentColor = AppColor.white
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
