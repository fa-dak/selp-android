package com.kosa.selp.shared.composable.gift

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.kosa.selp.features.home.presentation.screen.GiftPackage
import com.kosa.selp.shared.theme.AppColor

@Composable
fun GiftPackageRowCard(
    giftPackage: GiftPackage,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val giftCount = giftPackage.gifts.size
    val mainGift = giftPackage.gifts.firstOrNull()

    Surface(
        modifier = modifier
            .width(220.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(20.dp)),
        onClick = onClick,
        color = AppColor.white,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                mainGift?.let { gift ->
                    AsyncImage(
                        model = gift.imageUrl,
                        contentDescription = gift.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )
                } ?: run {
                    // 이미지가 없을 때 기본 배경
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColor.primary.copy(alpha = 0.1f),
                                        AppColor.secondary.copy(alpha = 0.1f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = AppColor.primary.copy(alpha = 0.3f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }


                if (giftCount > 1) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(
                                AppColor.black.copy(alpha = 0.6f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+${giftCount - 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColor.white
                        )
                    }
                }


                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .size(32.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    AppColor.primary,
                                    AppColor.secondary
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = AppColor.white,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 하단 정보 영역
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 선물 아이템 미리보기
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-6).dp)
                    ) {
                        giftPackage.gifts.take(4).forEachIndexed { index, gift ->
                            AsyncImage(
                                model = gift.imageUrl,
                                contentDescription = gift.title,
                                modifier = Modifier
                                    .size(48.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .border(1.5.dp, AppColor.white, CircleShape)
                                    .zIndex((4 - index).toFloat()),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (giftCount > 4) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(AppColor.surface)
                                    .border(1.5.dp, AppColor.white, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppColor.textSecondary
                                )
                            }
                        }
                    }

                    // 총 가격 (옵션)
                    if (giftPackage.gifts.isNotEmpty()) {
                        val totalPrice = giftPackage.gifts.sumOf { it.price }
                        if (totalPrice > 0) {
                            Text(
                                text = "${
                                    totalPrice.toString().reversed().chunked(3).joinToString(",")
                                        .reversed()
                                }원",
                                style = MaterialTheme.typography.labelMedium,
                                color = AppColor.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "보기",
                                tint = AppColor.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

