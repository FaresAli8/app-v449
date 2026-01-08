package com.expert.procalculator.model

data class CalculatorState(
    val expression: String = "",
    val result: String = "",
    val history: List<String> = emptyList(),
    val isError: Boolean = false
)