package com.kosa.selp.features.survey.viewModel

data class SurveyState(
    val step: SurveyStep = SurveyStep.BUDGET,
    val budget: Int? = null,
    val gender: String? = null,
    val age: String? = null,
    val relationship: String? = null,
    val preferences: List<String> = emptyList(),
    val freeInput: String? = null,
    val isSubmitting: Boolean = false,
    val submissionError: String? = null
)


