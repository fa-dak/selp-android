package com.kosa.selp.features.survey.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.data.request.GiftBundleSaveRequestDto
import com.kosa.selp.features.gift.data.request.GiftItemReplaceRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.domain.usecase.RecommendGiftBundleUseCase
import com.kosa.selp.features.gift.domain.usecase.ReplaceGiftItemUseCase
import com.kosa.selp.features.gift.domain.usecase.SaveGiftBundleUseCase
import com.kosa.selp.features.survey.model.AnniversaryType
import com.kosa.selp.features.survey.presentation.state.SurveyEvent
import com.kosa.selp.features.survey.presentation.state.SurveyState
import com.kosa.selp.features.survey.presentation.state.SurveyStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val recommendGiftBundleUseCase: RecommendGiftBundleUseCase,
    private val replaceGiftItemUseCase: ReplaceGiftItemUseCase,
    private val saveGiftBundleUseCase: SaveGiftBundleUseCase
) : ViewModel() {

    private val _loadingItemIds = MutableStateFlow<Set<Long>>(emptySet())
    val loadingItemIds = _loadingItemIds.asStateFlow()

    private val _uiState = MutableStateFlow(SurveyState())
    val uiState = _uiState.asStateFlow()

    private val _recommendedGiftBundles = MutableStateFlow<List<GiftBundleItemResponseDto>?>(null)
    val recommendedGiftBundles: StateFlow<List<GiftBundleItemResponseDto>?> =
        _recommendedGiftBundles
    private val stepOrder = listOf(
        SurveyStep.BUDGET,
        SurveyStep.ANNIVERSARY,
        SurveyStep.RELATIONSHIP,
        SurveyStep.CATEGORY,
        SurveyStep.AGE,
        SurveyStep.GENDER,
        SurveyStep.COMPLETE
    )

    fun onEvent(event: SurveyEvent) {
        when (event) {
            is SurveyEvent.BudgetSelected -> update { copy(budget = event.budget) }
            is SurveyEvent.AnniversarySelected -> update {
                val anniv = AnniversaryType.fromCode(event.anniversary.uppercase())
                copy(anniversary = anniv?.code)
            }

            is SurveyEvent.GenderSelected -> update {
                copy(gender = event.gender)
            }

            is SurveyEvent.AgeRangeSelected -> update { copy(ageRange = event.ageRange) }
            is SurveyEvent.RelationshipSelected -> update { copy(relationship = event.relationship) }
            is SurveyEvent.CategoriesSelected -> update { copy(categories = event.categories) }
            is SurveyEvent.UserMessageEntered -> update { copy(userMessage = event.userMessage) }

            SurveyEvent.NextClicked -> moveToNextStep()
            SurveyEvent.BackClicked -> moveToPreviousStep()

            SurveyEvent.SubmitClicked -> {
                update { copy(step = SurveyStep.COMPLETE) }
                submitSurvey()
            }
        }
    }

    private fun moveToNextStep() {
        val currentIndex = stepOrder.indexOf(uiState.value.step)
        if (currentIndex < stepOrder.lastIndex) {
            update { copy(step = stepOrder[currentIndex + 1]) }
        }
    }

    private fun moveToPreviousStep() {
        val currentIndex = stepOrder.indexOf(uiState.value.step)
        if (currentIndex > 0) {
            update { copy(step = stepOrder[currentIndex - 1]) }
        }
    }

    private fun submitSurvey() {
        val state = uiState.value

        val request = GiftBundleRecommendRequestDto(
            ageRange = state.ageRange ?: 20,
            anniversaryType = state.anniversary.orEmpty(),
            categories = state.categories,
            relation = state.relationship.orEmpty(),
            gender = state.gender.orEmpty(),
            budget = state.budget ?: 10000,
            userMessage = state.userMessage.orEmpty()
        )

        viewModelScope.launch {
            update { copy(isSubmitting = true, submissionError = null) }
            try {
                val result = recommendGiftBundleUseCase(request)
                _recommendedGiftBundles.value = result
            } catch (e: Exception) {
                update {
                    copy(
                        submissionError = "추천 결과를 가져오는데 실패했어요"
                    )
                }
            }
        }
    }

    fun replaceGiftItem(target: GiftBundleItemResponseDto) {
        val state = uiState.value
        val request = GiftItemReplaceRequestDto(
            productId = target.id,
            ageRange = state.ageRange,
            anniversaryType = state.anniversary?.uppercase() ?: "ANNIVERSARY",
            category = target.category,
            relation = state.relationship,
            gender = state.gender,
            price = target.price,
            userMessage = state.userMessage
        )

        viewModelScope.launch {
            _loadingItemIds.update { it + target.id }


            try {
                val replacement = replaceGiftItemUseCase(request)
                _recommendedGiftBundles.update { currentList ->
                    currentList?.map {
                        if (it.id == target.id) {
                            replacement
                        } else it
                    }
                }
            } catch (e: Exception) {
                Log.e("SurveyViewModel", "선물 재추천 실패", e)
            } finally {
                _loadingItemIds.update { it - target.id }
            }
        }
    }

    fun saveGiftBundle(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        val state = uiState.value
        val gifts = recommendedGiftBundles.value ?: return

        val request = GiftBundleSaveRequestDto(
            giftIds = gifts.map { it.id },
            ageRange = state.ageRange ?: 20,
            anniversaryType = state.anniversary.orEmpty(),
            categories = state.categories,
            relation = state.relationship.orEmpty(),
            gender = state.gender.orEmpty(),
            detail = state.userMessage.orEmpty()
        )

        viewModelScope.launch {
            runCatching {
                saveGiftBundleUseCase(request)
            }.onSuccess {
                onSuccess()
            }.onFailure { e ->
                Log.e("SurveyViewModel", "선물 꾸러미 저장 실패", e)
                onFailure(e)
            }
        }
    }

    private fun update(reducer: SurveyState.() -> SurveyState) {
        _uiState.update { it.reducer() }
    }
}
