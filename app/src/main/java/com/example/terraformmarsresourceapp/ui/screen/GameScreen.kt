package com.example.terraformmarsresourceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.terraformmarsresourceapp.ui.viewmodel.GameViewModel
import com.example.terraformmarsresourceapp.data.model.Player

@Composable
fun GameScreen(
    sessionId: String,
    onBack: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val gameSession by viewModel.gameSession.collectAsState()
    val players by viewModel.players.collectAsState()
    val activePlayer by viewModel.activePlayer.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    remember(sessionId) {
        viewModel.loadSession(sessionId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header with back button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.padding(end = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        "← Back",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        gameSession?.name ?: "Game",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "Generation ${gameSession?.generationCount ?: 0}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Tab Row
        if (!gameSession?.players.isNullOrEmpty()) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("My Resources") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("All Players") }
                )
            }
        }

        // Content
        when (selectedTab) {
            0 -> {
                if (activePlayer != null && gameSession != null) {
                    MyResourcesTab(
                        player = activePlayer!!,
                        gameSessionId = sessionId,
                        viewModel = viewModel,
                        onApplyProduction = {
                            viewModel.applyProduction(sessionId)
                        }
                    )
                }
            }
            1 -> {
                AllPlayersTab(
                    players = players,
                    onPlayerSwitch = { playerId ->
                        viewModel.switchActivePlayer(playerId, sessionId)
                        selectedTab = 0
                    }
                )
            }
        }
    }
}

@Composable
fun MyResourcesTab(
    player: Player,
    gameSessionId: String,
    viewModel: GameViewModel,
    onApplyProduction: () -> Unit
) {
    // TODO do this properly...
    //var showConfirmDialog by remember { mutableStateOf(false) }
    // var showConfirmDialog = false

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        item {
            // Terraforming Rating Section
            Text(
                "Terraforming Rating",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ResourceRow(
                        label = "TR",
                        value = player.terraformingRating,
                        onChange = { newValue ->
                            viewModel.updateTerraformingRating(gameSessionId, player.id, newValue)
                        },
                        increments = listOf(1)
                    )
                    Text(
                        text = "TR Income: +${player.terraformingRating} MEuro/gen",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // Resources Section
            Text(
                "Resources",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ResourceRow(
                        label = "MEuro",
                        value = player.resources.megacredits,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "megacredits", newValue)
                        },
                        increments = listOf(1, 5, 10)
                    )
                    ResourceRow(
                        label = "Steel",
                        value = player.resources.steel,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "steel", newValue)
                        },
                        increments = listOf(1, 5, 10)
                    )
                    ResourceRow(
                        label = "Titanium",
                        value = player.resources.titanium,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "titanium", newValue)
                        },
                        increments = listOf(1, 5, 10)
                    )
                    ResourceRow(
                        label = "Plants",
                        value = player.resources.plants,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "plants", newValue)
                        },
                        increments = listOf(1, 5, 10)
                    )
                    ResourceRow(
                        label = "Energy",
                        value = player.resources.energy,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "energy", newValue)
                        },
                        increments = listOf(1, 5, 10)
                    )
                    ResourceRow(
                        label = "Heat",
                        value = player.resources.heat,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "heat", newValue)
                        },
                        increments = listOf(1, 5, 10)
                    )
                }
            }

            // Production Section
            Text(
                "Production (per generation)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ResourceRow(
                        label = "MEuro/gen",
                        value = player.resources.megacreditsProduction,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "megacreditsProduction", newValue)
                        },
                        increments = listOf(1),
                        minValue = -5
                    )
                    ResourceRow(
                        label = "Steel/gen",
                        value = player.resources.steelProduction,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "steelProduction", newValue)
                        },
                        increments = listOf(1)
                    )
                    ResourceRow(
                        label = "Titanium/gen",
                        value = player.resources.titaniumProduction,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "titaniumProduction", newValue)
                        },
                        increments = listOf(1)
                    )
                    ResourceRow(
                        label = "Plants/gen",
                        value = player.resources.plantsProduction,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "plantsProduction", newValue)
                        },
                        increments = listOf(1)
                    )
                    ResourceRow(
                        label = "Energy/gen",
                        value = player.resources.energyProduction,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "energyProduction", newValue)
                        },
                        increments = listOf(1)
                    )
                    ResourceRow(
                        label = "Heat/gen",
                        value = player.resources.heatProduction,
                        onChange = { newValue ->
                            viewModel.updateResourceValue(gameSessionId, player.id, "heatProduction", newValue)
                        },
                        increments = listOf(1)
                    )
                }
            }

            // Next Generation Button
            Button(
                onClick = onApplyProduction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Next Generation",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }

    // Confirmation Dialog
//    if (showConfirmDialog) {
//        AlertDialog(
//            onDismissRequest = { showConfirmDialog = false },
//            title = { Text("Continue to Next Generation?", fontWeight = FontWeight.Bold) },
//            text = {
//                Text(
//                    "This will:\n" +
//                    "• Apply all production rates\n" +
//                    "• Convert all energy to heat\n" +
//                    "• Increment generation counter\n\n" +
//                    "Are you sure?"
//                )
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showConfirmDialog = false
//                        onApplyProduction()
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.primary
//                    )
//                ) {
//                    Text("Continue")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showConfirmDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
}

@Composable
fun AllPlayersTab(
    players: List<Player>,
    onPlayerSwitch: (String) -> Unit
) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        item {
            Text(
                "Players Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        items(players.size) { index ->
            val player = players[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                player.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "TR: ${player.terraformingRating}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Button(
                            onClick = { onPlayerSwitch(player.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Switch")
                        }
                    }

                    // Resource Summary
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            ResourceSummaryLine(
                                label = "MEuro",
                                value = player.resources.megacredits,
                                production = player.resources.megacreditsProduction + player.terraformingRating
                            )
                            ResourceSummaryLine(
                                label = "Steel",
                                value = player.resources.steel,
                                production = player.resources.steelProduction
                            )
                            ResourceSummaryLine(
                                label = "Titanium",
                                value = player.resources.titanium,
                                production = player.resources.titaniumProduction
                            )
                            ResourceSummaryLine(
                                label = "Plants",
                                value = player.resources.plants,
                                production = player.resources.plantsProduction
                            )
                            ResourceSummaryLine(
                                label = "Energy",
                                value = player.resources.energy,
                                production = player.resources.energyProduction
                            )
                            ResourceSummaryLine(
                                label = "Heat",
                                value = player.resources.heat,
                                production = player.resources.heatProduction
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResourceSummaryLine(
    label: String,
    value: Int,
    production: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            modifier = Modifier.width(70.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            value.toString(),
            modifier = Modifier.width(50.dp),
            fontSize = 12.sp
        )
        Text(
            "(+$production/gen)",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun ResourceRow(
    label: String,
    value: Int,
    onChange: (Int) -> Unit,
    increments: List<Int> = listOf(1),
    minValue: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            modifier = Modifier.width(70.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        // Value display
        Surface(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                value.toString(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Card(
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {

            // Increment buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                // horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
                horizontalArrangement = Arrangement.End
            ) {
                // Plus buttons
                for (inc in increments) {
                    Button(
                        onClick = { onChange(value + inc) },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text("+$inc", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Spacer(modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Minus buttons
                for (inc in increments) {
                    Button(
                        onClick = { onChange(maxOf(minValue, value - inc)) },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text("-$inc", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

        }
    }
}

