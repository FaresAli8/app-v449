package com.expert.procalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expert.procalculator.R
import com.expert.procalculator.ui.components.CalculatorButton
import com.expert.procalculator.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen() {
    val viewModel = viewModel<CalculatorViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // History Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.history_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = stringResource(R.string.clear_history),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable { viewModel.clearHistory() }
            )
        }
        
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(state.history) { item ->
                Text(
                    text = item,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 18.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.useHistoryItem(item) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f), shape = MaterialTheme.shapes.medium)
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = state.expression.ifEmpty { "0" },
                style = MaterialTheme.typography.displayMedium,
                color = if(state.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End,
                maxLines = 2
            )
            if (state.result.isNotEmpty()) {
                Text(
                    text = "= ${state.result}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Keypad
        val buttonSpacing = 12.dp
        
        // Helper to create rows
        @Composable
        fun ButtonRow(buttons: List<Triple<String, Float, Int>>) {
            // Triple: Symbol, Weight, Type (0:Num, 1:Op, 2:Action)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                buttons.forEach { (symbol, weight, type) ->
                    CalculatorButton(
                        symbol = symbol,
                        modifier = Modifier
                            .weight(weight)
                            .aspectRatio(if(weight > 1f) 2f else 1f),
                        isOperator = type == 1,
                        isAction = type == 2,
                        onClick = { viewModel.onAction(symbol) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(buttonSpacing))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            ButtonRow(listOf(
                Triple("AC", 1f, 2), Triple("DEL", 1f, 2), Triple("%", 1f, 1), Triple("÷", 1f, 1)
            ))
            ButtonRow(listOf(
                Triple("7", 1f, 0), Triple("8", 1f, 0), Triple("9", 1f, 0), Triple("×", 1f, 1)
            ))
            ButtonRow(listOf(
                Triple("4", 1f, 0), Triple("5", 1f, 0), Triple("6", 1f, 0), Triple("-", 1f, 1)
            ))
            ButtonRow(listOf(
                Triple("1", 1f, 0), Triple("2", 1f, 0), Triple("3", 1f, 0), Triple("+", 1f, 1)
            ))
            // Advanced Row + Zero
            ButtonRow(listOf(
                Triple("√", 1f, 1), Triple("^", 1f, 1), Triple("(", 1f, 1), Triple(")", 1f, 1)
            ))
            ButtonRow(listOf(
                Triple("0", 2f, 0), Triple(".", 1f, 0), Triple("=", 1f, 2)
            ))
        }
    }
}