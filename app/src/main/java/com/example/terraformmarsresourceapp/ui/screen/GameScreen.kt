package com.example.terraformmarsresourceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            TextButton(onClick = onBack) {
                Text("← Back")
            }
            Text(modifier = Modifier.weight(1f), text = gameSession?.name ?: "Game")
            Text("Gen: ${gameSession?.generationCount ?: 0}")
        }

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
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Terraforming Rating", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
            ResourceRow(
                label = "TR",
                value = player.terraformingRating,
                onChange = { newValue ->
                    viewModel.updateTerraformingRating(gameSessionId, player.id, newValue)
                }
            )
            Text(text = "TR Income: +${player.terraformingRating} MEuro/gen", modifier = Modifier.padding(bottom = 16.dp))

            Text("Resources", fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))

            ResourceRow(
                label = "MEuro",
                value = player.resources.megacredits,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "megacredits", newValue)
                }
            )

            ResourceRow(
                label = "Steel",
                value = player.resources.steel,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "steel", newValue)
                }
            )

            ResourceRow(
                label = "Titanium",
                value = player.resources.titanium,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "titanium", newValue)
                }
            )

            ResourceRow(
                label = "Plants",
                value = player.resources.plants,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "plants", newValue)
                }
            )

            ResourceRow(
                label = "Energy",
                value = player.resources.energy,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "energy", newValue)
                }
            )

            ResourceRow(
                label = "Heat",
                value = player.resources.heat,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "heat", newValue)
                }
            )

            Text("Production", fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))

            ResourceRow(
                label = "MEuro/gen",
                value = player.resources.megacreditsProduction,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "megacreditsProduction", newValue)
                }
            )

            ResourceRow(
                label = "Steel/gen",
                value = player.resources.steelProduction,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "steelProduction", newValue)
                }
            )

            ResourceRow(
                label = "Titanium/gen",
                value = player.resources.titaniumProduction,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "titaniumProduction", newValue)
                }
            )

            ResourceRow(
                label = "Plants/gen",
                value = player.resources.plantsProduction,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "plantsProduction", newValue)
                }
            )

            ResourceRow(
                label = "Energy/gen",
                value = player.resources.energyProduction,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "energyProduction", newValue)
                }
            )

            ResourceRow(
                label = "Heat/gen",
                value = player.resources.heatProduction,
                onChange = { newValue ->
                    viewModel.updateResourceValue(gameSessionId, player.id, "heatProduction", newValue)
                }
            )

            Button(
                onClick = onApplyProduction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 32.dp)
            ) {
                Text("Next Generation", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun AllPlayersTab(
    players: List<Player>,
    onPlayerSwitch: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Players Overview", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))
        }
        items(players.size) { index ->
            val player = players[index]
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(player.name, fontSize = 16.sp)
                            Text("TR: ${player.terraformingRating}")
                            Text("MEuro: ${player.resources.megacredits} (+${player.resources.megacreditsProduction}/gen, +${player.terraformingRating}/gen from TR)")
                            Text("Steel: ${player.resources.steel} (+${player.resources.steelProduction}/gen)")
                            Text("Titanium: ${player.resources.titanium} (+${player.resources.titaniumProduction}/gen)")
                            Text("Plants: ${player.resources.plants} (+${player.resources.plantsProduction}/gen)")
                            Text("Energy: ${player.resources.energy} (+${player.resources.energyProduction}/gen)")
                            Text("Heat: ${player.resources.heat} (+${player.resources.heatProduction}/gen)")
                        }
                        TextButton(onClick = { onPlayerSwitch(player.id) }) {
                            Text("Switch")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResourceRow(
    label: String,
    value: Int,
    onChange: (Int) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, modifier = Modifier.weight(1f))
        Row {
            TextButton(onClick = { onChange(maxOf(0, value - 1)) }) { Text("-") }
            Text(value.toString(), modifier = Modifier.padding(horizontal = 8.dp))
            TextButton(onClick = { onChange(value + 1) }) { Text("+") }
        }
    }
}

