package com.kosa.selp.shared.composable.gift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.home.presentation.screen.GiftPackage
import com.kosa.selp.shared.theme.AppColor

@Composable
fun GiftPackageRowList(
    packages: List<GiftPackage>,
    modifier: Modifier = Modifier,
    onClick: (GiftPackage) -> Unit = {}
) {
    if (packages.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = AppColor.primary.copy(alpha = 0.3f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "최근에 만든 선물꾸러미가 없어요",
                style = MaterialTheme.typography.titleMedium,
                color = AppColor.textPrimary
            )
            Text(
                text = "선물 추천을 받아 꾸러미를 만들어보세요!",
                style = MaterialTheme.typography.bodySmall,
                color = AppColor.textSecondary
            )
        }
    } else {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(packages) { giftPackage ->
                GiftPackageRowCard(
                    giftPackage = giftPackage,
                    modifier = Modifier.fillParentMaxWidth(),
                    onClick = { onClick(giftPackage) }
                )
            }
        }
    }
}
