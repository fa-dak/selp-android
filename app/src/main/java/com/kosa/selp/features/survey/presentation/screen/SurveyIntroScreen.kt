package com.kosa.selp.features.survey.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kosa.selp.R
import com.kosa.selp.shared.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyIntroScreen(
    onBack: () -> Unit,
    onStartSurvey: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = White
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.selp_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Image(
                    painter = painterResource(id = R.drawable.selp_character),
                    contentDescription = null,
                    modifier = Modifier.size(160.dp)
                )

                Text(
                    text = "1분 만에 찾는,\n특정한 사람에게 딱 맞는 선물",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "@@@@의 맞춤 선물 추천",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "간단한 설문을 통해\n받는 사람에게 꼭 맞는 선물을 추천해드려요!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 시작 버튼
                Button(
                    onClick = onStartSurvey,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("선물 고르러 가기")
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
