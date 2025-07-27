package com.kosa.selp.shared.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor

@Composable
fun Modifier.shimmerPlaceholder(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offsetX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerAnim"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            AppColor.surface,
            AppColor.divider,
            AppColor.surface
        ),
        start = Offset.Zero,
        end = Offset(x = offsetX, y = offsetX)
    )

    return this.background(brush, shape = RoundedCornerShape(6.dp))
}
