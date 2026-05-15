package com.example.terraformmarsresourceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.terraformmarsresourceapp.ui.viewmodel.SessionListViewModel

@Composable
fun SessionListScreen(
    onSessionSelected: (String) -> Unit,
    viewModel: SessionListViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessions.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Terraforming Mars - Game Sessions")

        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Create New Session")
        }

        if (showCreateDialog) {
            CreateSessionDialog(
                onConfirm = { name ->
                    viewModel.createSession(name) { sessionId ->
                        showCreateDialog = false
                        onSessionSelected(sessionId)
                    }
                },
                onDismiss = { showCreateDialog = false }
            )
        }

        LazyColumn {
            items(sessions) { session ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = session.name, maxLines = 1)
                            Text(text = "Generation: ${session.generationCount}", maxLines = 1)
                        }
                        Button(
                            onClick = { onSessionSelected(session.id) }
                        ) {
                            Text("Load")
                        }
                        TextButton(
                            onClick = { viewModel.deleteSession(session.id) }
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateSessionDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("Game 1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Session") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Session Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(name) }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

