package com.kosa.selp.features.gift.composable


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.shared.theme.AppColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBundleScaffold(
    onBack: () -> Unit,
    onFabClick: () -> Unit,
    giftBundle: GiftBundleDetailResponseDto,
    content: @Composable (PaddingValues) -> Unit
) {
    val (userName, ageGroup, relation) = giftBundle

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = AppColor.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.white)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = AppColor.primary,
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Edit, contentDescription = "메시지 추천", tint = Color.White)
            }
        },
        containerColor = AppColor.white
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = buildAnnotatedString {
                    append("${userName}님의 ")
                    withStyle(style = SpanStyle(color = AppColor.primary)) {
                        append("${ageGroup} ${relation}를\n")
                    }
                    append("위한 추천 선물 결과입니다")
                },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AppColor.textPrimary
            )

            content(PaddingValues(top = 8.dp))
        }
    }
}
