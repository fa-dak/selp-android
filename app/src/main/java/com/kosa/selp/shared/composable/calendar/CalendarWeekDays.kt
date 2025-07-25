package com.kosa.selp.shared.composable.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kosa.selp.shared.theme.AppColor

@Composable
fun CalendarWeekDays() {
    val weekdays = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekdays.forEachIndexed { index, day ->
            Text(
                text = day,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = if (index == 0) AppColor.primary else AppColor.textPrimary
                )
            )
        }
    }
}
