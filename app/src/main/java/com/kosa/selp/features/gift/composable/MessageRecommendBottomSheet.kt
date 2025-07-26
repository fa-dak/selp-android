package com.kosa.selp.features.gift.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShortText
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.gift.model.ToneOption
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageRecommendBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val density = LocalDensity.current
    val windowHeightPx = LocalWindowInfo.current.containerSize.height
    val windowHeightDp = with(density) { windowHeightPx.toDp() }
    val scope = rememberCoroutineScope()

    val imeInsets = WindowInsets.ime.asPaddingValues()

    val toneOptions = listOf(
        ToneOption(
            title = "감동형",
            description = "진심을 담아 따뜻하게",
            icon = Icons.Filled.Favorite,
            iconColor = Color(0xFFFF6B81),
            gradient = listOf(Color(0xFFFF9A9E), Color(0xFFFAD0C4))
        ),
        ToneOption(
            title = "유머형",
            description = "재치와 센스를 담은 한마디",
            icon = Icons.Filled.EmojiEmotions,
            iconColor = Color(0xFFFFC107),
            gradient = listOf(Color(0xFFFFE57F), Color(0xFFFFC107))
        ),
        ToneOption(
            title = "간결형",
            description = "짧고 핵심만 전달해요",
            icon = Icons.AutoMirrored.Filled.ShortText,
            iconColor = Color(0xFF00BCD4),
            gradient = listOf(Color(0xFFB2EBF2), Color(0xFF00ACC1))
        ),
        ToneOption(
            title = "직접 입력할게요",
            description = "내가 원하는 스타일로 작성할래요",
            icon = Icons.Filled.Edit,
            iconColor = AppColor.primary,
            gradient = listOf(AppColor.secondary, AppColor.primary)
        )
    )

    var selectedTone by remember { mutableStateOf<String?>(null) }
    var customMessage by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onDismiss()
            }
        },
        sheetState = sheetState,
        containerColor = Color.White,
        tonalElevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(windowHeightDp * 0.8f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "어떤 스타일의 메시지를 전하고 싶나요?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AppColor.textPrimary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                if (selectedTone == "직접 입력할게요") {
                    val customOption = toneOptions.last()
                    item {
                        ToneOptionCard(
                            title = customOption.title,
                            description = customOption.description,
                            icon = customOption.icon,
                            iconColor = customOption.iconColor,
                            gradient = customOption.gradient,
                            isSelected = true,
                            onClick = { /* 이미 선택됨 */ }
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = customMessage,
                            onValueChange = { customMessage = it },
                            placeholder = { Text("예: 생일 진심으로 축하해!") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { selectedTone = null },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = AppColor.primary
                                ),
                                elevation = null
                            ) {
                                Text("← 다시 선택하기")
                            }
                        }
                    }
                } else {
                    items(toneOptions) { tone ->
                        ToneOptionCard(
                            title = tone.title,
                            description = tone.description,
                            icon = tone.icon,
                            iconColor = tone.iconColor,
                            gradient = tone.gradient,
                            isSelected = selectedTone == tone.title,
                            onClick = { selectedTone = tone.title }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            Button(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                enabled = selectedTone != null && (selectedTone != "직접 입력할게요" || customMessage.isNotBlank()),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.primary,
                    contentColor = AppColor.white
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = imeInsets.calculateBottomPadding().coerceAtLeast(16.dp))
                    .height(48.dp)
            ) {
                Text("메시지 추천 받기", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
