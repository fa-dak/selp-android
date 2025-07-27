package com.kosa.selp.features.gift.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.kosa.selp.features.gift.model.GiftBundleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GiftBundleUiViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GiftBundleUiState())
    val uiState: StateFlow<GiftBundleUiState> = _uiState.asStateFlow()

    fun showBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = true)
    }

    fun dismissBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = false)
    }

    fun showOverlay() {
        _uiState.value = _uiState.value.copy(
            showOverlay = true,
            typingTexts = List(3) { "" }
        )
    }

    fun updateTyping(index: Int, text: String) {
        val updated = _uiState.value.typingTexts.toMutableList().apply {
            this[index] = text
        }
        _uiState.value = _uiState.value.copy(typingTexts = updated)
    }

    fun resetOverlay() {
        _uiState.value = _uiState.value.copy(
            showOverlay = false,
            typingTexts = List(3) { "" }
        )
    }

    fun collectUiState(): StateFlow<GiftBundleUiState> = uiState
}
