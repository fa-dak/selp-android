package com.kosa.selp.features.survey.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.data.request.GiftBundleSaveFromCalendarRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.domain.usecase.RecommendGiftBundleUseCase
import com.kosa.selp.features.gift.domain.usecase.SaveGiftBundleFromCalendarUseCase
import com.kosa.selp.features.mypage.data.repository.MyPageRepository
import com.kosa.selp.features.mypage.model.Contact
import com.kosa.selp.features.survey.presentation.state.LiteSurveyEvent
import com.kosa.selp.features.survey.presentation.state.LiteSurveyState
import com.kosa.selp.features.survey.presentation.state.SurveyStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiteSurveyViewModel @Inject constructor(
    private val repository: MyPageRepository,
    private val recommendGiftBundleUseCase: RecommendGiftBundleUseCase,
    private val saveGiftBundleFromCalendarUseCase: SaveGiftBundleFromCalendarUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _loadingItemIds = MutableStateFlow<Set<Long>>(emptySet())
    val loadingItemIds = _loadingItemIds.asStateFlow()

    val contactId: Long = savedStateHandle.get<String>("contactId")?.toLongOrNull()
        ?: error("contactId가 null이거나 Long으로 변환할 수 없습니다.")
    val anniversaryFromArgs: String? = savedStateHandle["anniversary"]
    val eventId: Long? = savedStateHandle.get<String>("eventId")?.toLongOrNull()


    private val _uiState = MutableStateFlow(LiteSurveyState())
    val uiState: StateFlow<LiteSurveyState> = _uiState

    private val _recommendedGiftBundles = MutableStateFlow<List<GiftBundleItemResponseDto>?>(null)
    val recommendedGiftBundles: StateFlow<List<GiftBundleItemResponseDto>?> =
        _recommendedGiftBundles

    init {
        loadContactAndInit()
    }


    private fun loadContactAndInit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            runCatching {
                repository.getMyReceiverInfoDetail(contactId)
            }.onSuccess { contact ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        age = contact.age,
                        gender = contact.gender.takeIf { it != "NONE" },
                        relationship = contact.relationship,
                        categories = contact.preferences,
                        anniversary = anniversaryFromArgs,
                        stepList = getDynamicSteps(contact)
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun submitSurvey() {
        val state = _uiState.value

        val request = GiftBundleRecommendRequestDto(
            ageRange = state.age ?: 20,
            anniversaryType = state.anniversary ?: "ANNIVERSARY",
            categories = state.categories,
            relation = state.relationship.orEmpty(),
            gender = state.gender.orEmpty(),
            budget = state.budget ?: 10000,
            userMessage = ""
        )


        viewModelScope.launch {
            runCatching {
                val result = recommendGiftBundleUseCase(request)
                _recommendedGiftBundles.value = result
            }.onFailure {
                println("❌ 추천 실패: ${it.message}")
                it.printStackTrace()
            }
        }
    }

    fun replaceGiftItem(item: GiftBundleItemResponseDto) {
        viewModelScope.launch {
            _loadingItemIds.update { it + item.id }

            runCatching {
                recommendGiftBundleUseCase(
                    GiftBundleRecommendRequestDto(
                        ageRange = uiState.value.age ?: 20,
                        anniversaryType = "ANNIVERSARY",
                        categories = uiState.value.categories,
                        relation = uiState.value.relationship.orEmpty(),
                        gender = uiState.value.gender.orEmpty(),
                        budget = uiState.value.budget ?: 10000,
                        userMessage = ""
                    )
                )
            }.onSuccess { newGifts ->
                val replaced = _recommendedGiftBundles.value?.toMutableList() ?: mutableListOf()
                val index = replaced.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    replaced[index] = newGifts.firstOrNull() ?: item
                    _recommendedGiftBundles.value = replaced
                }
            }.onFailure {
                println("재추천 실패: ${it.localizedMessage}")
            }.also {
                _loadingItemIds.update { it - item.id }
            }
        }
    }

    fun saveGiftBundleFromCalendar(
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val gifts = recommendedGiftBundles.value ?: return
        val eventId = this.eventId ?: return

        val request = GiftBundleSaveFromCalendarRequestDto(
            giftIds = gifts.map { it.id },
            eventId = eventId
        )

        viewModelScope.launch {
            runCatching {
                saveGiftBundleFromCalendarUseCase(request)
            }.onSuccess {
                onSuccess()
            }.onFailure { e ->
                println("📛 캘린더 기반 선물 저장 실패: ${e.localizedMessage}")
                onFailure(e)
            }
        }
    }

    fun onEvent(event: LiteSurveyEvent) {
        when (event) {
            is LiteSurveyEvent.BudgetEntered -> {
                _uiState.update { it.copy(budget = event.budget) }
            }

            is LiteSurveyEvent.GenderSelected -> {
                _uiState.update { it.copy(gender = event.gender) }
            }

            is LiteSurveyEvent.AgeRangeSelected -> {
                _uiState.update { it.copy(age = event.age) }
            }

            is LiteSurveyEvent.RelationshipSelected -> {
                _uiState.update { it.copy(relationship = event.relationship) }
            }

            is LiteSurveyEvent.CategorySelected -> {
                _uiState.update { it.copy(categories = event.categories) }
            }

            is LiteSurveyEvent.AnniversarySelected -> {
                _uiState.update { it.copy(anniversary = event.anniversary) }
            }

            is LiteSurveyEvent.NextClicked -> {
                moveToNextStep()
            }

            is LiteSurveyEvent.BackClicked -> {
                moveToPreviousStep()
            }

            is LiteSurveyEvent.SubmitClicked -> {
                _uiState.update { it.copy(step = SurveyStep.COMPLETE) }
                submitSurvey()
            }
        }
    }


    private fun moveToNextStep() {
        val current = _uiState.value.step
        val steps = _uiState.value.stepList
        val nextIndex = steps.indexOf(current) + 1
        if (nextIndex in steps.indices) {
            _uiState.update { it.copy(step = steps[nextIndex]) }
        }
    }

    private fun moveToPreviousStep() {
        val current = _uiState.value.step
        val steps = _uiState.value.stepList
        val prevIndex = steps.indexOf(current) - 1
        if (prevIndex in steps.indices) {
            _uiState.update { it.copy(step = steps[prevIndex]) }
        }
    }

    private fun getDynamicSteps(contact: Contact): List<SurveyStep> = buildList {
        add(SurveyStep.BUDGET)
        if (contact.age == null) add(SurveyStep.AGE)
        if (contact.gender.isNullOrBlank()) add(SurveyStep.GENDER)
        if (contact.relationship == null) add(SurveyStep.RELATIONSHIP)
        if (contact.preferences.isEmpty()) add(SurveyStep.CATEGORY)
        if (anniversaryFromArgs == null) add(SurveyStep.ANNIVERSARY)
        add(SurveyStep.COMPLETE)
    }
}
