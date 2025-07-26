package com.kosa.selp.shared.composable.gift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.home.presentation.screen.GiftPackage

@Composable
fun GiftPackageRowList(
    packages: List<GiftPackage>,
    modifier: Modifier = Modifier,
    onClick: (GiftPackage) -> Unit = {}
) {
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