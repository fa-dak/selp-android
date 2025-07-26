package com.kosa.selp.features.gift.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.gift.model.AgeGroupGift
import com.kosa.selp.features.gift.repository.AgeGroupGiftRepository
import kotlinx.coroutines.launch

class AgeGroupGiftViewModel : ViewModel() {

    var selectedAgeGroup by mutableStateOf("10대")
        private set

    var gifts by mutableStateOf<List<AgeGroupGift>>(emptyList())
        private set

    private val repository = AgeGroupGiftRepository()

    init {
        loadGifts("10대")
    }

    fun selectAgeGroup(ageGroup: String) {
        selectedAgeGroup = ageGroup
        loadGifts(ageGroup)
    }

    private fun loadGifts(ageGroup: String) {
        viewModelScope.launch {
            gifts = repository.getGiftsByAgeGroup(ageGroup)
        }
    }
}