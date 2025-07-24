package com.kosa.selp.shared.composable.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.kosa.selp.features.gift.model.Gift
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.R
import java.util.Locale

@Composable
fun GiftCard(gift: Gift) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 1.dp,
                color = AppColor.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = gift.imageUrl,
                    placeholder = painterResource(R.drawable.selp_placeholder),
                    error = painterResource(R.drawable.selp_error)
                ),
                contentDescription = gift.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = gift.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = AppColor.textSecondary
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = String.format(Locale.KOREA, "%,d원", gift.price),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppColor.textPrimary
                )
            )
        }
    }
}

