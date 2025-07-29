package com.kosa.selp.features.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor

@Composable
fun BottomAddButton(
    onAddSchedule: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                color = AppColor.background,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onAddSchedule)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "추가",
                tint = AppColor.textPrimary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = "추가",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColor.textPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}