package com.kosa.selp.features.survey.presentation.state


data class LiteSurveyState(
    val step: SurveyStep = SurveyStep.BUDGET,
    val stepList: List<SurveyStep> = listOf(SurveyStep.BUDGET, SurveyStep.COMPLETE),
    val budget: Int? = null,
    val anniversary: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    val relationship: String? = null,
    val categories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)