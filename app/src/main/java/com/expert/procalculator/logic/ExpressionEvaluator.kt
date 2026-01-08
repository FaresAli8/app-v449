package com.expert.procalculator.logic

import java.util.Stack
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A custom logic engine to evaluate mathematical expressions safely.
 * Uses Shunting-yard algorithm to parse and evaluate.
 * Supports: +, -, *, /, %, ^ (power), sqrt, (), decimals.
 */
object ExpressionEvaluator {

    fun evaluate(expression: String): Double {
        val tokens = tokenize(expression)
        val rpn = shuntingYard(tokens)
        return calculateRPN(rpn)
    }

    private fun tokenize(expression: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            if (c.isDigit() || c == '.') {
                val sb = StringBuilder()
                while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                    sb.append(expression[i])
                    i++
                }
                tokens.add(sb.toString())
                continue
            } else if ("+-*/%^()√".contains(c)) {
                tokens.add(c.toString())
            }
            i++
        }
        return tokens
    }

    private fun shuntingYard(tokens: List<String>): List<String> {
        val outputQueue = mutableListOf<String>()
        val operatorStack = Stack<String>()

        val precedence = mapOf(
            "+" to 1, "-" to 1,
            "*" to 2, "/" to 2, "%" to 2,
            "^" to 3, "√" to 3
        )

        for (token in tokens) {
            if (token.first().isDigit()) {
                outputQueue.add(token)
            } else if (token == "√") {
                 operatorStack.push(token)
            } else if (precedence.containsKey(token)) {
                while (operatorStack.isNotEmpty() && operatorStack.peek() != "(" &&
                    (precedence[operatorStack.peek()] ?: 0) >= (precedence[token] ?: 0)) {
                    outputQueue.add(operatorStack.pop())
                }
                operatorStack.push(token)
            } else if (token == "(") {
                operatorStack.push(token)
            } else if (token == ")") {
                while (operatorStack.isNotEmpty() && operatorStack.peek() != "(") {
                    outputQueue.add(operatorStack.pop())
                }
                if (operatorStack.isNotEmpty()) operatorStack.pop() // Pop "("
            }
        }
        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.pop())
        }
        return outputQueue
    }

    private fun calculateRPN(rpn: List<String>): Double {
        val stack = Stack<Double>()

        for (token in rpn) {
            if (token.first().isDigit()) {
                stack.push(token.toDouble())
            } else {
                if (stack.isEmpty()) throw IllegalArgumentException("Invalid")
                
                if (token == "√") {
                    val a = stack.pop()
                    stack.push(sqrt(a))
                } else {
                    if (stack.size < 2) throw IllegalArgumentException("Invalid")
                    val b = stack.pop()
                    val a = stack.pop()
                    when (token) {
                        "+" -> stack.push(a + b)
                        "-" -> stack.push(a - b)
                        "*" -> stack.push(a * b)
                        "/" -> stack.push(a / b)
                        "%" -> stack.push(a % b)
                        "^" -> stack.push(a.pow(b))
                    }
                }
            }
        }
        if (stack.size != 1) throw IllegalArgumentException("Invalid")
        return stack.pop()
    }
}