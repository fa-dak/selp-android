package com.kosa.selp.features.gift.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GiftBundleRecommendMessageUiViewModel : ViewModel() {

    private val _recommendedMessages = MutableStateFlow<List<String>>(emptyList())
    val recommendedMessages: StateFlow<List<String>> = _recommendedMessages

    private val _selectedTone = MutableStateFlow<String?>(null)
    val selectedTone: StateFlow<String?> = _selectedTone

    private val _customMessage = MutableStateFlow("")
    val customMessage: StateFlow<String> = _customMessage

    private val _canGenerate = MutableStateFlow(false)
    val canGenerate: StateFlow<Boolean> = _canGenerate

    private fun updateCanGenerate() {
        _canGenerate.value =
            _selectedTone.value != null &&
                    (_selectedTone.value != "직접 입력할게요" || _customMessage.value.isNotBlank())
    }

    fun selectTone(tone: String) {
        _selectedTone.value = tone
        updateCanGenerate()
    }

    fun updateCustomMessage(text: String) {
        _customMessage.value = text
        updateCanGenerate()
    }

    fun reset() {
        _selectedTone.value = null
        _customMessage.value = ""
        _recommendedMessages.value = emptyList()
        _canGenerate.value = false
    }

}
