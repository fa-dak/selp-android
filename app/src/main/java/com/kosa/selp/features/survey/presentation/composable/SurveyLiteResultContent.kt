package com.kosa.selp.features.survey.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.survey.presentation.viewModel.LiteSurveyViewModel
import com.kosa.selp.shared.composable.gift.GiftCarouselMultiBrowse
import com.kosa.selp.shared.theme.AppColor

@Composable
fun SurveyLiteResultContent(
    gifts: List<GiftBundleItemResponseDto>,
    navController: NavController,
    viewModel: LiteSurveyViewModel
) {
    val isEmpty = gifts.isEmpty()
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                if (isEmpty) {
                    append("추천 선물을 찾지 못했어요 😢\n")
                    append("다음엔 더 좋은 선물을 찾아드릴게요!")
                } else {
                    append("받는 분을 위한\n")
                    withStyle(style = SpanStyle(color = AppColor.primary)) {
                        append("추천 선물 결과")
                    }
                    append("입니다")
                }
            },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColor.textPrimary,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!isEmpty) {
            GiftCarouselMultiBrowse(
                gifts = gifts,
                onReplaceClick = { gift ->
                    viewModel.replaceGiftItem(gift)
                },
                loadingItemIds = viewModel.loadingItemIds.collectAsState().value
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
//                    if (!isSaving) {
//                        isSaving = true
//                        viewModel.saveGiftBundle(
//                            onSuccess = {
//                                coroutineScope.launch {
//                                    delay(300)
//                                    isSaving = false
//                                    navController.navigate("home")
//                                }
//                            },
//                            onFailure = { error ->
//                                coroutineScope.launch {
//                                    delay(200)
//                                    isSaving = false
//                                    println("저장 실패: ${error.message}")
//                                }
//                            }
//                        )
//                    }
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSaving) AppColor.primary.copy(alpha = 0.7f) else AppColor.primary,
                    disabledContainerColor = AppColor.primary.copy(alpha = 0.7f)
                )
            ) {
                if (isSaving) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = AppColor.white,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "저장 중...",
                            color = AppColor.white,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    Text(
                        "이렇게 결정!",
                        color = AppColor.white,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.secondary)
            ) {
                Text(
                    "다시할게요",
                    color = AppColor.white,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
