package com.kosa.selp.shared.composable.gift

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kosa.selp.R
import com.kosa.selp.features.home.presentation.screen.GiftItem
import com.kosa.selp.shared.theme.AppColor

@Composable
fun GiftCard(
    gift: GiftItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(gift.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = gift.title,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.selp_background),
                error = painterResource(R.drawable.selp_background),
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = gift.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = AppColor.textSecondary,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1
        )

        Text(
            text = gift.price,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = AppColor.textPrimary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}