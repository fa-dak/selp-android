package com.kosa.selp.features.gift.composable.bundle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor

@Composable
fun ToneOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    gradient: List<Color>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundBrush = Brush.horizontalGradient(
        colors = if (isSelected) gradient else listOf(AppColor.white, AppColor.white)
    )
    val effectiveBorderColor = if (isSelected) gradient.last() else AppColor.divider

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, effectiveBorderColor),
        tonalElevation = if (isSelected) 2.dp else 0.dp,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Unspecified
    ) {
        Box(
            modifier = Modifier
                .background(if (isSelected) backgroundBrush else SolidColor(AppColor.white))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(AppColor.white, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = AppColor.textPrimary
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColor.textPrimary
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = AppColor.textDisabled
                )
            }
        }
    }
}
