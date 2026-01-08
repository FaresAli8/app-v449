package com.expert.procalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.expert.procalculator.ui.CalculatorScreen
import com.expert.procalculator.ui.theme.ProCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}