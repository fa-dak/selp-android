package com.kosa.selp.features.survey.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.domain.usecase.RecommendGiftBundleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    val stepOrder = listOf(
        SurveyStep.BUDGET,
        SurveyStep.RELATIONSHIP,
        SurveyStep.PREFERENCE,
        SurveyStep.AGE,
        SurveyStep.GENDER,
        SurveyStep.COMPLETE
    )

    fun onEvent(event: SurveyEvent) {
        when (event) {
            is SurveyEvent.BudgetSelected -> _uiState.update { it.copy(budget = event.budget) }
            is SurveyEvent.GenderSelected -> _uiState.update { it.copy(gender = event.gender) }
            is SurveyEvent.AgeSelected -> _uiState.update { it.copy(age = event.age) }
            is SurveyEvent.RelationshipSelected -> _uiState.update { it.copy(relationship = event.relationship) }
            is SurveyEvent.PreferencesSelected -> _uiState.update { it.copy(preferences = event.preferences) }
            is SurveyEvent.FreeInputEntered -> _uiState.update { it.copy(freeInput = event.text) }
            SurveyEvent.NextClicked -> goToNextStep()
            SurveyEvent.BackClicked -> goToPreviousStep()
            SurveyEvent.SubmitClicked -> {
                submitSurvey()
            }
        }
    }

    private fun goToNextStep() {
        val currentIndex = stepOrder.indexOf(_uiState.value.step)
        if (currentIndex < stepOrder.size - 1) {
            _uiState.update { it.copy(step = stepOrder[currentIndex + 1]) }
        }
    }

    private fun goToPreviousStep() {
        val currentIndex = stepOrder.indexOf(_uiState.value.step)
        if (currentIndex > 0) {
            _uiState.update { it.copy(step = stepOrder[currentIndex - 1]) }
        }
    }


    private fun submitSurvey() {
        val state = _uiState.value
        Log.i("SurveyViewModel", "submitSurvey called")

        val request = GiftBundleRecommendRequestDto(
            ageRange = "30대",
            anniversaryType = "생일",
            categories = listOf("fashion", "living", "desert"),
            relation = "아내",
            gender = "여성",
            budget = 80000,
            userMessage = "내 아내는 따뜻한 감성을 좋아해"

//            ageRange = state.age ?: "",
//            anniversaryType = "birthday",
//            categories = state.preferences,
//            relation = state.relationship ?: "",
//            gender = state.gender ?: "",
//            budget = state.budget ?: 0,
//            userMessage = state.freeInput ?: ""
        )
        Log.i("SurveyViewModel", "Request: $request")


        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submissionError = null) }

            try {
                val result = recommendGiftBundleUseCase(request)
                Log.i("SurveyViewModel", "추천 성공: $result")

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        step = SurveyStep.COMPLETE,
//                        recommendedGifts = result
                    )
                }
            } catch (e: Exception) {
                Log.e("SurveyViewModel", "추천 실패", e)
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        submissionError = "추천 결과를 가져오는데 실패했어요"
                    )
                }
            }
        }
    }
}
