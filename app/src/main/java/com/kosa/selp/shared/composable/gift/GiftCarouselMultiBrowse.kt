package com.kosa.selp.shared.composable.gift

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.shared.theme.AppColor
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftCarouselMultiBrowse(gifts: List<GiftBundleItemResponseDto>) {
    val windowSize = LocalWindowInfo.current.containerSize
    val screenWidth = with(LocalDensity.current) { windowSize.width.toDp() }
    val screenHeight = with(LocalDensity.current) { windowSize.height.toDp() }

    val cardWidth = screenWidth * if (gifts.size == 1) 0.88f else 0.72f
    val itemSpacing = if (gifts.size == 1) 4.dp else 12.dp
    val contentHorizontalPadding = if (gifts.size == 1) 8.dp else 16.dp

    val cardHeight = screenHeight * 0.33f

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { gifts.size },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp),
        preferredItemWidth = cardWidth,
        itemSpacing = itemSpacing,
        contentPadding = PaddingValues(horizontal = contentHorizontalPadding)
    ) { index ->
        val gift = gifts[index]

        Box(
            modifier = Modifier
                .height(cardHeight)
                .maskClip(MaterialTheme.shapes.extraLarge)
        ) {
            Image(
                painter = rememberAsyncImagePainter(gift.imagePath),
                contentDescription = gift.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            AssistChip(
                onClick = {},
                label = {
                    Text(
                        "재추천",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColor.white
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, AppColor.primary),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = AppColor.primary,
                    labelColor = Color.White
                )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xCC000000))
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = gift.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1
                )
                Text(
                    text = "${DecimalFormat("#,###").format(gift.price)}원",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f))
                )
            }
        }
    }
}
