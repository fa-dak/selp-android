package com.kosa.selp.features.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.calendar.config.CalendarConfig
import com.kosa.selp.shared.theme.AppColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        for (week in cells.chunked(7)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                week.forEach { date ->
                    val isSelected = date != null && isSameDay(date, config.selectedDate)

                    val background = when {
                        isSelected -> AppColor.primary
                        else -> AppColor.white
                    }

                    val textColor = when {
                        isSelected -> AppColor.white
                        else -> AppColor.textPrimary
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(0.9f)
                            .padding(4.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                enabled = date != null
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
//                                Text(
//                                    text = Calendar.getInstance().apply { time = date }
//                                        .get(Calendar.DAY_OF_MONTH).toString(),
//                                    color = textColor,
//                                    style = MaterialTheme.typography.bodyLarge,
//                                    textAlign = TextAlign.Center
//                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = Calendar.getInstance().apply { time = date }
                                            .get(Calendar.DAY_OF_MONTH).toString(),
                                        color = textColor,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )

                                    // 점 (이벤트가 있을 경우만 표시)
                                    if (date != null && config.events.any { event ->
                                            val eventDate = event.eventDate
                                            if (eventDate != null) {
                                                try {
                                                    val parsedDate = dateFormatter.parse(eventDate)
                                                    isSameDay(parsedDate, date)
                                                } catch (e: Exception) {
                                                    false
                                                }
                                            } else {
                                                false
                                            }
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 2.dp)
                                                .size(6.dp)
                                                .background(
                                                    AppColor.primary,
                                                    shape = RoundedCornerShape(50)
                                                )
                                        )
                                    }
                                }
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