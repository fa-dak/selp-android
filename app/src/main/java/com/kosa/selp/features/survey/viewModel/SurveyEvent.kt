package com.kosa.selp.features.survey.viewModel

sealed interface SurveyEvent {
    data class BudgetSelected(val budget: Int?) : SurveyEvent
    data class GenderSelected(val gender: String) : SurveyEvent
    data class AgeSelected(val age: String) : SurveyEvent
    data class RelationshipSelected(val relationship: String) : SurveyEvent
    data class PreferencesSelected(val preferences: List<String>) : SurveyEvent
    data class FreeInputEntered(val text: String) : SurveyEvent
    object NextClicked : SurveyEvent
    object BackClicked : SurveyEvent
    object SubmitClicked : SurveyEvent
}