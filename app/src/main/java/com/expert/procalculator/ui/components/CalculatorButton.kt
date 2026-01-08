package com.expert.procalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.expert.procalculator.ui.theme.OperatorOrange
import com.expert.procalculator.ui.theme.AccentTeal

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isAction: Boolean = false, // =, AC, DEL
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isAction && symbol == "=" -> AccentTeal
        isAction -> OperatorOrange
        isOperator -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val contentColor = when {
        isAction || isOperator -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(
            text = symbol,
            fontSize = 26.sp,
            color = contentColor,
            fontWeight = FontWeight.Bold
        )
    }
}