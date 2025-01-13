package com.kushal.mealapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculatorPopup(private val context: Context) {

    private var input = ""
    private var operator = ""
    private var history = ""
    private lateinit var display: TextView

    fun showCalculatorPopup() {
        // Inflate the calculator layout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.calculator_popup, null)

        // Initialize views
        display = popupView.findViewById(R.id.calculatorDisplay)
        val closeButton = popupView.findViewById<Button>(R.id.calculatorCloseButton)

        // Define button IDs
        val buttonIds = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide,
            R.id.buttonEquals, R.id.buttonClear
        )

        // Assign click listeners to each button
        buttonIds.forEach { id ->
            val button = popupView.findViewById<Button>(id)
            button.setOnClickListener { handleButtonClick(it) }
        }

        // Configure the PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true // Allows dismissing with the back button
        )

        // Close button functionality
        closeButton.setOnClickListener {
            popupWindow.dismiss()
        }

        // Show the popup window centered
        popupWindow.showAtLocation(
            (context as AppCompatActivity).findViewById(android.R.id.content),
            android.view.Gravity.CENTER,
            0,
            0
        )
    }

    /**
     * Handle button click events
     */
    private fun handleButtonClick(view: View) {
        when (view.id) {
            R.id.buttonClear -> clearCalculator()
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide -> handleOperator(view as Button)
            R.id.buttonEquals -> calculateResult()
            else -> handleNumberInput(view as Button)
        }
    }

    /**
     * Handle number and digit button clicks
     */
    private fun handleNumberInput(button: Button) {
        input += button.text.toString()
        display.text = "$history$input"
    }

    /**
     * Handle operator button clicks
     */
    private fun handleOperator(button: Button) {
        if (input.isNotEmpty()) {
            history += "$input ${button.text} "
            input = ""
            display.text = history
        }
    }

    /**
     * Perform the calculation and display the result
     */
    private fun calculateResult() {
        if (input.isNotEmpty()) {
            history += input
            try {
                val result = evaluateExpression(history)
                display.text = "$history = $result"
                resetCalculator(result)
            } catch (e: Exception) {
                display.text = "Error"
                resetCalculator()
            }
        }
    }

    /**
     * Evaluate the mathematical expression using BODMAS
     */
    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ").filter { it.isNotEmpty() }
        val values = mutableListOf<Double>()
        val operators = mutableListOf<String>()

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            when {
                token.toDoubleOrNull() != null -> values.add(token.toDouble())
                token in listOf("+", "-") -> {
                    while (operators.isNotEmpty() && operators.last() in listOf("+", "-", "*", "/")) {
                        val result = applyOperator(operators.removeLast(), values.removeAt(values.lastIndex - 1), values.removeLast())
                        values.add(result)
                    }
                    operators.add(token)
                }
                token in listOf("*", "/") -> {
                    while (operators.isNotEmpty() && operators.last() in listOf("*", "/")) {
                        val result = applyOperator(operators.removeLast(), values.removeAt(values.lastIndex - 1), values.removeLast())
                        values.add(result)
                    }
                    operators.add(token)
                }
            }
            i++
        }

        while (operators.isNotEmpty()) {
            val result = applyOperator(operators.removeLast(), values.removeAt(values.lastIndex - 1), values.removeLast())
            values.add(result)
        }

        return values.last()
    }

    /**
     * Apply a single operator to two operands
     */
    private fun applyOperator(operator: String, a: Double, b: Double): Double {
        return when (operator) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> a / b
            else -> throw IllegalArgumentException("Unknown operator: $operator")
        }
    }

    /**
     * Clear the calculator state
     */
    private fun clearCalculator() {
        input = ""
        operator = ""
        history = ""
        display.text = "0"
    }

    /**
     * Reset the calculator state after a calculation
     */
    private fun resetCalculator(result: Double? = null) {
        input = result?.toString() ?: ""
        operator = ""
        history = ""
    }
}
