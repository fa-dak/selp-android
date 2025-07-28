package com.kosa.selp.features.survey.presentation.state

data class SurveyState(
    val step: SurveyStep = SurveyStep.BUDGET,
    val budget: Int? = null,
    val anniversary: String? = null,
    val gender: String? = null,
    val ageRange: String? = null,
    val relationship: String? = null,
    val categories: List<String> = emptyList(),
    val userMessage: String? = null,
    val isSubmitting: Boolean = false,
    val submissionError: String? = null
)