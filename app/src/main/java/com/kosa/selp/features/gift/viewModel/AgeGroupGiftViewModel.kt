package com.kosa.selp.features.gift.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.gift.model.AgeGroupGift
import com.kosa.selp.features.gift.repository.AgeGroupGiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AgeGroupGiftViewModel @Inject constructor(
    private val repository: AgeGroupGiftRepository
) : ViewModel()  {

    var selectedAgeGroup by mutableStateOf("10s")
        private set

    private val _gifts = MutableStateFlow<List<AgeGroupGift>>(emptyList())
    val gifts: StateFlow<List<AgeGroupGift>> = _gifts.asStateFlow()

    init {
        loadGifts("10s")
    }

    fun selectAgeGroup(ageGroup: String) {
        selectedAgeGroup = ageGroup
        loadGifts(ageGroup)
    }

    fun loadGifts(ageGroup: String) {
        viewModelScope.launch {
            val result = repository.getGiftsByAgeGroup(ageGroup)
            _gifts.value = result
        }
    }
}