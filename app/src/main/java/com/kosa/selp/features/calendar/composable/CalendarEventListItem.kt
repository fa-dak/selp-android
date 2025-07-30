package com.kosa.selp.features.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.features.survey.model.AnniversaryType
import com.kosa.selp.shared.theme.AppColor

@Composable
fun CalendarEventListItem(
    event: EventListResponseDto,
    modifier: Modifier = Modifier
) {

    val title = event.eventName?.takeIf { it.isNotBlank() } ?: "제목없음"
    val relationTag = event.receiverNickname?.takeIf { it.isNotBlank() }
    val typeTag = event.eventType?.takeIf { it.isNotBlank() }
    val typeLabel = AnniversaryType.fromCode(event.eventType ?: "")?.label

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF9C88FF))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = AppColor.textPrimary
                    )
                )

                if (relationTag != null || typeTag != null) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        relationTag?.let {
                            Tag(
                                text = it,
                                backgroundColor = Color(0xFFD0E8FF),
                                textColor = Color(0xFF1565C0)
                            )
                        }

                        typeLabel?.let {
                            Tag(
                                text = it,
                                backgroundColor = Color(0xFFFFECB3),
                                textColor = Color(0xFFEF6C00)
                            )
                        }
                    }
                }
            }
        }
    }
}
