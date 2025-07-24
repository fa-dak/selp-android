package com.kosa.selp.features.survey.viewModel

object SurveyProgress {
    fun getRatio(step: SurveyStep): Float = when (step) {
        SurveyStep.BUDGET -> 0f
        SurveyStep.RELATIONSHIP -> 0.2f
        SurveyStep.PREFERENCE -> 0.4f
        SurveyStep.AGE -> 0.6f
        SurveyStep.GENDER -> 0.8f
        SurveyStep.COMPLETE -> 1f
    }
}