package com.kosa.selp.features.gift.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ToneOption(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val gradient: List<Color>
)