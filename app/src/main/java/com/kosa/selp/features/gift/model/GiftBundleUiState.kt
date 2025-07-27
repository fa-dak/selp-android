package com.kosa.selp.features.gift.model

data class GiftBundleUiState(
    val showBottomSheet: Boolean = false,
    val showOverlay: Boolean = false,
    val typingTexts: List<String> = List(3) { "" }
)