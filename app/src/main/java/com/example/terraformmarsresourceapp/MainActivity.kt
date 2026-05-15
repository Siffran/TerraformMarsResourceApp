package com.example.terraformmarsresourceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.terraformmarsresourceapp.ui.screen.GameScreen
import com.example.terraformmarsresourceapp.ui.screen.SessionListScreen
import com.example.terraformmarsresourceapp.ui.theme.TerraformMarsResourceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerraformMarsResourceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val selectedSession = remember { mutableStateOf<String?>(null) }

                    if (selectedSession.value == null) {
                        SessionListScreen(
                            onSessionSelected = { sessionId ->
                                selectedSession.value = sessionId
                            }
                        )
                    } else {
                        GameScreen(
                            sessionId = selectedSession.value!!,
                            onBack = { selectedSession.value = null }
                        )
                    }
                }
            }
        }
    }
}

