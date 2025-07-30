package com.kosa.selp.features.survey.presentation.state

sealed interface LiteSurveyEvent {
    data class BudgetEntered(val budget: Int) : LiteSurveyEvent
    data class GenderSelected(val gender: String) : LiteSurveyEvent
    data class AgeRangeSelected(val age: Int) : LiteSurveyEvent
    data class RelationshipSelected(val relationship: String) : LiteSurveyEvent
    data class CategorySelected(val categories: List<String>) : LiteSurveyEvent
    data class AnniversarySelected(val anniversary: String) : LiteSurveyEvent
    object NextClicked : LiteSurveyEvent
    object BackClicked : LiteSurveyEvent
    object SubmitClicked : LiteSurveyEvent
}