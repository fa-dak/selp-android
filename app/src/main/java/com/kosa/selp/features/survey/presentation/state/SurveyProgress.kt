package com.kosa.selp.features.survey.presentation.state

object SurveyProgress {
    fun getRatio(step: SurveyStep): Float = when (step) {
        SurveyStep.BUDGET -> 0f
        SurveyStep.ANNIVERSARY -> 0.1f
        SurveyStep.RELATIONSHIP -> 0.2f
        SurveyStep.CATEGORY -> 0.4f
        SurveyStep.AGE -> 0.6f
        SurveyStep.GENDER -> 0.8f
        SurveyStep.COMPLETE -> 1f
    }
}