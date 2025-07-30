package com.kosa.selp.shared.composable.gift

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kosa.selp.R
import com.kosa.selp.features.home.presentation.screen.GiftItem
import com.kosa.selp.shared.theme.AppColor
import java.text.DecimalFormat

@Composable
fun GiftCard(
    gift: GiftItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        color = AppColor.white,
        shadowElevation = 8.dp
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(gift.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = gift.title,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.selp_background),
                error = painterResource(R.drawable.selp_background),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            )

            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = gift.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = AppColor.textPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${DecimalFormat("#,###").format(gift.price)}원",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = AppColor.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}