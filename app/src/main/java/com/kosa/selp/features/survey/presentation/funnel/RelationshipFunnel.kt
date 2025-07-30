package com.kosa.selp.features.survey.presentation.funnel

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.survey.presentation.state.SurveyEvent
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.domain.model.Relationship
import com.kosa.selp.shared.theme.AppColor

@Composable
fun RelationshipFunnel(
    viewModel: SurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val options = Relationship.entries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "선물할 상대방과의 사이는 어떤 사이인가요?", style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ), color = AppColor.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))


        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = state.relationship == option.displayName

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) AppColor.primary.copy(alpha = 0.1f) else AppColor.white,
                    label = "ButtonBackground"
                )
                val borderColor by animateColorAsState(
                    targetValue = if (isSelected) AppColor.primary else AppColor.divider,
                    label = "ButtonBorder"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) AppColor.primary else AppColor.textPrimary,
                    label = "ButtonText"
                )

                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(SurveyEvent.RelationshipSelected(option.displayName))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = backgroundColor, contentColor = contentColor
                    ),
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Text(
                        text = option.displayName, style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onNext() },
            enabled = state.relationship != null, colors = ButtonDefaults.buttonColors(
                containerColor = AppColor.primary, contentColor = AppColor.white
            ), shape = RoundedCornerShape(12.dp), modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("다음", style = MaterialTheme.typography.labelLarge)
        }
    }
}

