package com.expert.procalculator.viewmodel

import androidx.lifecycle.ViewModel
import com.expert.procalculator.logic.ExpressionEvaluator
import com.expert.procalculator.model.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()
    
    // Limits the decimals to 8 places
    private val decimalFormat = DecimalFormat("#.########")

    fun onAction(action: String) {
        when (action) {
            "AC" -> _state.update { it.copy(expression = "", result = "", isError = false) }
            "DEL" -> deleteLast()
            "=" -> calculate()
            else -> appendInput(action)
        }
    }

    fun clearHistory() {
        _state.update { it.copy(history = emptyList()) }
    }
    
    fun useHistoryItem(item: String) {
        // Extract the result part from history string "Expression = Result"
        if (item.contains("=")) {
            val result = item.substringAfter("=").trim()
            _state.update { it.copy(expression = it.expression + result) }
        }
    }

    private fun deleteLast() {
        val currentExpr = _state.value.expression
        if (currentExpr.isNotEmpty()) {
            _state.update { it.copy(expression = currentExpr.dropLast(1)) }
        }
    }

    private fun appendInput(input: String) {
        // Prevent multiple decimals in one number logic could be here, 
        // but for simplicity we allow typing and let evaluator catch errors or user be smart.
        // We will do basic replacement for UI symbols to Math symbols if needed.
        
        val newExpr = if (_state.value.isError) input else _state.value.expression + input
        _state.update { it.copy(expression = newExpr, isError = false) }
    }

    private fun calculate() {
        val expr = _state.value.expression
        if (expr.isEmpty()) return

        try {
            // Replace visual symbols with logic symbols if different
            // In this app, we use same symbols mostly. 'x' might be '*' in UI.
            val sanitizedExpr = expr.replace("×", "*").replace("÷", "/")
            
            val resultDouble = ExpressionEvaluator.evaluate(sanitizedExpr)
            val resultStr = decimalFormat.format(resultDouble)

            val historyItem = "$expr = $resultStr"
            val newHistory = listOf(historyItem) + _state.value.history

            _state.update {
                it.copy(
                    expression = resultStr,
                    result = "",
                    history = newHistory.take(20), // Keep last 20
                    isError = false
                )
            }
        } catch (e: Exception) {
            _state.update { it.copy(isError = true, result = "Error") }
        }
    }
}