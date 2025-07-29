package com.kosa.selp.features.gift.viewModel

import androidx.lifecycle.ViewModel
import com.kosa.selp.features.gift.repository.GiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GiftViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {

    suspend fun getGiftUrlById(giftId: Long): String? {
        return giftRepository.getGiftInfo(giftId).detailUrl
    }
}