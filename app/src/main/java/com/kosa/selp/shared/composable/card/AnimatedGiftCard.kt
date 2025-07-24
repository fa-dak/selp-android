package com.kosa.selp.shared.composable.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.*
import com.kosa.selp.features.gift.model.Gift

@Composable
fun AnimatedGiftCard(gift: Gift, visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(
            initialOffsetY = { it / 2 }, animationSpec = tween(500)
        )
    ) {
        GiftCard(gift = gift)
    }
}