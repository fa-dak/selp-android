package com.kosa.selp.features.survey.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.shared.composable.gift.GiftCarouselMultiBrowse
import com.kosa.selp.shared.theme.AppColor

@Composable
fun SurveyResultContent(
    gifts: List<GiftBundleItemResponseDto>,
    navController: NavController
) {
    val isEmpty = gifts.isEmpty()

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
                    append("보라님의 ")
                    withStyle(style = SpanStyle(color = AppColor.primary)) {
                        append("20대 친구를\n")
                    }
                    append("위한 추천 선물 결과입니다")
                }
            },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColor.textPrimary,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        if (!isEmpty) {
            GiftCarouselMultiBrowse(gifts)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.primary)
            ) {
                Text(
                    "이렇게 결정!",
                    color = AppColor.white,
                    style = MaterialTheme.typography.labelLarge
                )
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