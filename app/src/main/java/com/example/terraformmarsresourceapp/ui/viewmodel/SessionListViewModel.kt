package com.example.terraformmarsresourceapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terraformmarsresourceapp.data.model.GameSessionEntity
import com.example.terraformmarsresourceapp.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionListViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    val sessions: StateFlow<List<GameSessionEntity>> = sessionRepository.getAllSessions()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    fun createSession(name: String, playerCount: Int, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val sessionId = sessionRepository.createSession(name)

            repeat(playerCount) { index ->
                val playerName = "Player ${index + 1}"
                val playerId = sessionRepository.addPlayer(sessionId, playerName, initialTR = 20)

                // Set the player as active
                if (index == 0) {
                    sessionRepository.setActivePlayer(playerId, sessionId)
                }
            }
            onSuccess(sessionId)
        }
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            sessionRepository.deleteSession(sessionId)
        }
    }
}

