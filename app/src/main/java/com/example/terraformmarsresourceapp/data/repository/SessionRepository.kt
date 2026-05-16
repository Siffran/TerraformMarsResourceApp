package com.example.terraformmarsresourceapp.data.repository

import com.example.terraformmarsresourceapp.data.dao.GameSessionDao
import com.example.terraformmarsresourceapp.data.dao.PlayerDao
import com.example.terraformmarsresourceapp.data.model.GameSessionEntity
import com.example.terraformmarsresourceapp.data.model.PlayerEntity
import com.example.terraformmarsresourceapp.data.model.GameSession
import com.example.terraformmarsresourceapp.data.model.Player
import com.example.terraformmarsresourceapp.data.model.ResourceStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SessionRepository(
    private val gameSessionDao: GameSessionDao,
    private val playerDao: PlayerDao
) {
    fun getAllSessions(): Flow<List<GameSessionEntity>> = gameSessionDao.getAllSessions()

    fun getSession(id: String): Flow<GameSession?> = combine(
        gameSessionDao.getSession(id),
        playerDao.getPlayersBySession(id)
    ) { session, players ->
        if (session != null) {
            GameSession(
                id = session.id,
                name = session.name,
                createdAt = session.createdAt,
                lastModified = session.lastModified,
                generationCount = session.generationCount,
                players = players.map { it.toPlayer() }
            )
        } else null
    }

    suspend fun createSession(name: String): String {
        val session = GameSessionEntity(name = name)
        gameSessionDao.insert(session)
        return session.id
    }

    suspend fun updateSession(session: GameSessionEntity) {
        gameSessionDao.update(session)
    }

    suspend fun deleteSession(sessionId: String) {
        playerDao.deletePlayersBySession(sessionId)
        gameSessionDao.deleteById(sessionId)
    }

    suspend fun addPlayer(gameSessionId: String, playerName: String, initialTR: Int = 0): String {
        val player = PlayerEntity(
            gameSessionId = gameSessionId,
            name = playerName,
            terraformingRating = initialTR
        )
        playerDao.insert(player)
        return player.id
    }

    suspend fun updatePlayer(player: PlayerEntity) {
        playerDao.update(player)
    }

    suspend fun setActivePlayer(playerId: String, gameSessionId: String) {
        playerDao.deactivateAllPlayers(gameSessionId)
        playerDao.setActivePlayer(playerId)
    }

    fun getGamePlayers(gameSessionId: String): Flow<List<Player>> =
        playerDao.getPlayersBySession(gameSessionId).let { flow ->
            flow.combine(gameSessionDao.getSession(gameSessionId)) { players, session ->
                players.map { it.toPlayer() }
            }
        }
}

private fun PlayerEntity.toPlayer() = Player(
    id = id,
    name = name,
    terraformingRating = terraformingRating,
    resources = resources.toResourceState(),
    isActive = isActive
)

private fun ResourceStateEntity.toResourceState() = com.example.terraformmarsresourceapp.data.model.ResourceState(
    megacredits = megacredits,
    steel = steel,
    titanium = titanium,
    plants = plants,
    energy = energy,
    heat = heat,
    megacreditsProduction = megacreditsProduction,
    steelProduction = steelProduction,
    titaniumProduction = titaniumProduction,
    plantsProduction = plantsProduction,
    energyProduction = energyProduction,
    heatProduction = heatProduction
)

