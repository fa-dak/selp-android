package com.kosa.selp.features.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnniversaryList(
    anniversaries: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(anniversaries) { (title, dayName) ->
            AnniversaryCard(title = title, dayName = dayName)
        }
    }
}
