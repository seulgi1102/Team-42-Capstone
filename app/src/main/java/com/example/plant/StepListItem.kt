package com.example.plant

class StepListItem {
    private var stepTitle = ""
    private var stepContent = ""
    fun setStepTitle(stepT: String){
        stepTitle = stepT
    }
    fun getStepTitle(): String{
        return stepTitle
    }
    fun setStepContent(stepC: String){
        stepContent = stepC
    }
    fun getStepContent(): String{
        return stepContent
    }
}

