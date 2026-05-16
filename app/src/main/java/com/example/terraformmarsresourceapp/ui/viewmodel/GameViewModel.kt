package com.example.terraformmarsresourceapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terraformmarsresourceapp.data.model.GameSession
import com.example.terraformmarsresourceapp.data.model.Player
import com.example.terraformmarsresourceapp.data.model.PlayerEntity
import com.example.terraformmarsresourceapp.data.model.ResourceStateEntity
import com.example.terraformmarsresourceapp.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _gameSession = MutableStateFlow<GameSession?>(null)
    val gameSession: StateFlow<GameSession?> = _gameSession.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _activePlayer = MutableStateFlow<Player?>(null)
    val activePlayer: StateFlow<Player?> = _activePlayer.asStateFlow()

    fun loadSession(sessionId: String) {
        viewModelScope.launch {
            sessionRepository.getSession(sessionId).collect { session ->
                _gameSession.value = session
                if (session != null) {
                    _players.value = session.players
                    val active = session.players.firstOrNull { it.isActive }
                    _activePlayer.value = active ?: session.players.firstOrNull()
                }
            }
        }
    }

    fun addPlayer(gameSessionId: String, playerName: String) {
        viewModelScope.launch {
            sessionRepository.addPlayer(gameSessionId, playerName)
        }
    }

    fun switchActivePlayer(playerId: String, gameSessionId: String) {
        viewModelScope.launch {
            sessionRepository.setActivePlayer(playerId, gameSessionId)
        }
    }

    fun updateResourceValue(
        gameSessionId: String,
        playerId: String,
        resourceKey: String,
        value: Int
    ) {
        viewModelScope.launch {
            val currentSession = _gameSession.value ?: return@launch
            val player = currentSession.players.find { it.id == playerId } ?: return@launch

            val updatedResources = player.resources.let {
                when (resourceKey) {
                    "megacredits" -> it.copy(megacredits = value)
                    "steel" -> it.copy(steel = value)
                    "titanium" -> it.copy(titanium = value)
                    "plants" -> it.copy(plants = value)
                    "energy" -> it.copy(energy = value)
                    "heat" -> it.copy(heat = value)
                    "megacreditsProduction" -> it.copy(megacreditsProduction = value)
                    "steelProduction" -> it.copy(steelProduction = value)
                    "titaniumProduction" -> it.copy(titaniumProduction = value)
                    "plantsProduction" -> it.copy(plantsProduction = value)
                    "energyProduction" -> it.copy(energyProduction = value)
                    "heatProduction" -> it.copy(heatProduction = value)
                    else -> it
                }
            }

            val updatedPlayerEntity = PlayerEntity(
                id = player.id,
                gameSessionId = gameSessionId,
                name = player.name,
                isActive = player.isActive,
                terraformingRating = player.terraformingRating,
                resources = ResourceStateEntity(
                    megacredits = updatedResources.megacredits,
                    steel = updatedResources.steel,
                    titanium = updatedResources.titanium,
                    plants = updatedResources.plants,
                    energy = updatedResources.energy,
                    heat = updatedResources.heat,
                    megacreditsProduction = updatedResources.megacreditsProduction,
                    steelProduction = updatedResources.steelProduction,
                    titaniumProduction = updatedResources.titaniumProduction,
                    plantsProduction = updatedResources.plantsProduction,
                    energyProduction = updatedResources.energyProduction,
                    heatProduction = updatedResources.heatProduction
                )
            )

            sessionRepository.updatePlayer(updatedPlayerEntity)
        }
    }

    fun updateTerraformingRating(gameSessionId: String, playerId: String, value: Int) {
        viewModelScope.launch {
            val currentSession = _gameSession.value ?: return@launch
            val player = currentSession.players.find { it.id == playerId } ?: return@launch

            val updatedPlayerEntity = PlayerEntity(
                id = player.id,
                gameSessionId = gameSessionId,
                name = player.name,
                isActive = player.isActive,
                terraformingRating = value,
                resources = ResourceStateEntity(
                    megacredits = player.resources.megacredits,
                    steel = player.resources.steel,
                    titanium = player.resources.titanium,
                    plants = player.resources.plants,
                    energy = player.resources.energy,
                    heat = player.resources.heat,
                    megacreditsProduction = player.resources.megacreditsProduction,
                    steelProduction = player.resources.steelProduction,
                    titaniumProduction = player.resources.titaniumProduction,
                    plantsProduction = player.resources.plantsProduction,
                    energyProduction = player.resources.energyProduction,
                    heatProduction = player.resources.heatProduction
                )
            )

            sessionRepository.updatePlayer(updatedPlayerEntity)
        }
    }

    fun applyProduction(gameSessionId: String) {
        viewModelScope.launch {
            val currentSession = _gameSession.value ?: return@launch

            for (player in currentSession.players) {
                // Convert energy to heat first
                val energyConverted = player.resources.energy

                val newResources = player.resources.copy(
                    megacredits = player.resources.megacredits + player.resources.megacreditsProduction + (player.terraformingRating * 1),
                    steel = player.resources.steel + player.resources.steelProduction,
                    titanium = player.resources.titanium + player.resources.titaniumProduction,
                    plants = player.resources.plants + player.resources.plantsProduction,
                    energy = player.resources.energyProduction,
                    heat = player.resources.heat + player.resources.heatProduction + energyConverted
                )

                val updatedPlayerEntity = PlayerEntity(
                    id = player.id,
                    gameSessionId = gameSessionId,
                    name = player.name,
                    isActive = player.isActive,
                    terraformingRating = player.terraformingRating,
                    resources = ResourceStateEntity(
                        megacredits = newResources.megacredits,
                        steel = newResources.steel,
                        titanium = newResources.titanium,
                        plants = newResources.plants,
                        energy = newResources.energy,
                        heat = newResources.heat,
                        megacreditsProduction = newResources.megacreditsProduction,
                        steelProduction = newResources.steelProduction,
                        titaniumProduction = newResources.titaniumProduction,
                        plantsProduction = newResources.plantsProduction,
                        energyProduction = newResources.energyProduction,
                        heatProduction = newResources.heatProduction
                    )
                )

                sessionRepository.updatePlayer(updatedPlayerEntity)
            }

            // Increment generation counter
            val updatedSession = currentSession.let {
                com.example.terraformmarsresourceapp.data.model.GameSessionEntity(
                    id = it.id,
                    name = it.name,
                    createdAt = it.createdAt,
                    lastModified = System.currentTimeMillis(),
                    generationCount = it.generationCount + 1
                )
            }

            sessionRepository.updateSession(updatedSession)
        }
    }
}

