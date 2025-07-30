package com.kosa.selp.features.calendar.composable

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.shared.theme.AppColor

@Composable
fun CalendarEventOverlay(
    events: List<EventListResponseDto>,
    onDismiss: () -> Unit,
    onEventClick: (EventListResponseDto) -> Unit
) {
    BackHandler(onBack = onDismiss)

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.white)
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("전체 일정", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onDismiss) {
                    Text("닫기", color = AppColor.primary)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(events) { index, event ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEventClick(event) }
                    ) {
                        CalendarEventListItem(event)
                        if (index < events.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = AppColor.divider
                            )
                        }
                    }
                }
            }
        }
    }
}
