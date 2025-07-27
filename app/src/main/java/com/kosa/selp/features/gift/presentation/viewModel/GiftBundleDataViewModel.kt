package com.kosa.selp.features.gift.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.data.response.GiftItemDto
import com.kosa.selp.features.gift.domain.usecase.GetGiftBundleDetailUseCase
import com.kosa.selp.features.gift.domain.usecase.GetGiftBundleRecommendMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

val dummyGiftBundle = GiftBundleDetailResponseDto(
    userName = "보라",
    ageGroup = "20대",
    relation = "친구",
    giftList = listOf(
        GiftItemDto(
            id = "1",
            name = "향 좋은 디퓨저",
            imageUrl = "https://ldb-phinf.pstatic.net/20241226_124/1735176183750fjEhg_JPEG/%C8%A6%C5%D7%B5%F0%C4%C9%C0%CC%C5%A9_%281%29.jpg",
            price = "19,800원"
        ),
        GiftItemDto(
            id = "2",
            name = "레터링 케이크",
            imageUrl = "https://ldb-phinf.pstatic.net/20241226_81/1735176192690m2B0T_JPEG/%C8%AD%C0%CC%C6%AE%C8%A6%C5%D7%B5%F0.jpeg",
            price = "35,000원"
        )
    )
)

val dummyMessages = listOf(
    "네 마음을 닮은 디퓨저로 향기 가득한 하루를 선물해 보세요.",
    "달콤한 메시지와 함께 케이크로 특별한 날을 기념해요.",
    "무드등처럼 은은하게 당신을 응원하는 선물이에요."
)


@HiltViewModel
class GiftBundleDataViewModel @Inject constructor(
    private val getGiftBundleDetailUseCase: GetGiftBundleDetailUseCase,
    private val getRecommendedMessagesUseCase: GetGiftBundleRecommendMessagesUseCase
) : ViewModel() {


    private val _giftBundleData = MutableStateFlow<GiftBundleDetailResponseDto?>(null)
    val giftBundleData: StateFlow<GiftBundleDetailResponseDto?> = _giftBundleData.asStateFlow()

    private val _recommendedMessages = MutableStateFlow<List<String>>(emptyList())
    val recommendedMessages: StateFlow<List<String>> = _recommendedMessages.asStateFlow()

    fun loadGiftBundle(giftBundleId: String) {
        viewModelScope.launch {
//            val result = getGiftBundleDetailUseCase(giftBundleId)
            _giftBundleData.value = dummyGiftBundle
        }
    }

    fun loadRecommendedMessages(bundleId: String) {
        viewModelScope.launch {
//            val result = getRecommendedMessagesUseCase(bundleId)
            _recommendedMessages.value = dummyMessages
        }
    }

    fun resetMessages() {
        _recommendedMessages.value = emptyList()
    }
}