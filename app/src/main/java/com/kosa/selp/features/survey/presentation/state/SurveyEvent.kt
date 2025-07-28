package com.kosa.selp.features.survey.presentation.state

sealed interface SurveyEvent {
    data class BudgetSelected(val budget: Int?) : SurveyEvent
    data class AnniversarySelected(val anniversary: String) : SurveyEvent
    data class GenderSelected(val gender: String) : SurveyEvent
    data class AgeRangeSelected(val ageRange: String) : SurveyEvent
    data class RelationshipSelected(val relationship: String) : SurveyEvent
    data class CategoriesSelected(val categories: List<String>) : SurveyEvent
    data class UserMessageEntered(val userMessage: String) : SurveyEvent
    object NextClicked : SurveyEvent
    object BackClicked : SurveyEvent
    object SubmitClicked : SurveyEvent
}