package com.kosa.selp.features.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor

@Composable
fun AnniversaryList(
    anniversaries: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    if (anniversaries.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "다가오는 기념일이 없어요 🎉",
                color = AppColor.textSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    } else {
        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(start = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(anniversaries.size) { index ->
                val (title, dday) = anniversaries[index]
                AnniversaryCard(title = title, dayName = dday)
            }
        }
    }
}
