package com.kosa.selp.shared.composable.calendar


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.composable.calendar.model.CalendarConfig
import com.kosa.selp.shared.theme.AppColor
import java.util.Calendar
import java.util.Date

@Composable
fun CalendarMonthGrid(
    month: Date,
    config: CalendarConfig
) {
    val calendar = Calendar.getInstance().apply {
        time = month
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val startOffset = (firstDayOfWeek + 6) % 7

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val cells = buildList<Date?> {
        repeat(startOffset) { add(null) }
        repeat(daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, it + 1)
            add(calendar.time)
        }
        val extra = 7 - (size % 7)
        if (extra < 7) repeat(extra) { add(null) }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        for (week in cells.chunked(7)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                week.forEach { date ->
                    val ts = date?.time ?: 0L
                    val isDisabled = date != null && (
                            ts < config.range.first || ts > config.range.last ||
                                    (config.mode == "unavailable" && config.blockedTimestamps.contains(
                                        ts
                                    ))
                            )
                    val isSelected = date != null && isSameDay(date, config.selectedDate)

                    val background = when {
                        isSelected -> AppColor.primary
                        isDisabled -> AppColor.secondary
                        else -> AppColor.white
                    }

                    val textColor = when {
                        isSelected -> AppColor.white
                        isDisabled -> AppColor.textSecondary
                        else -> AppColor.textPrimary
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                enabled = date != null && !isDisabled
                            ) {
                                date?.let { config.onChange(it) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        background,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = Calendar.getInstance().apply { time = date }
                                        .get(Calendar.DAY_OF_MONTH).toString(),
                                    color = textColor,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun isSameDay(d1: Date?, d2: Date?): Boolean {
    if (d1 == null || d2 == null) return false
    val cal1 = Calendar.getInstance().apply { time = d1 }
    val cal2 = Calendar.getInstance().apply { time = d2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}
