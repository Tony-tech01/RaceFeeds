package com.example.racefeeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.racefeeds.ui.screens.Farm.FarmPage
import com.example.racefeeds.ui.theme.RaceFeedsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RaceFeedsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FarmPage(innerPadding = innerPadding)
                }
            }
        }
    }
}


