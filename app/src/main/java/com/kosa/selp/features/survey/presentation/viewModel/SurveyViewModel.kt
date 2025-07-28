package com.kosa.selp.features.survey.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.domain.usecase.RecommendGiftBundleUseCase
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
    private val recommendGiftBundleUseCase: RecommendGiftBundleUseCase
) : ViewModel() {


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
            is SurveyEvent.AnniversarySelected -> update { copy(anniversary = event.anniversary) }
            is SurveyEvent.GenderSelected -> update { copy(gender = event.gender) }
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
            ageRange = state.ageRange.orEmpty(),
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
                Log.e("SurveyViewModel", "추천 실패", e)
                update {
                    copy(
                        submissionError = "추천 결과를 가져오는데 실패했어요"
                    )
                }
            }
        }
    }

    private fun update(reducer: SurveyState.() -> SurveyState) {
        _uiState.update { it.reducer() }
    }
}
