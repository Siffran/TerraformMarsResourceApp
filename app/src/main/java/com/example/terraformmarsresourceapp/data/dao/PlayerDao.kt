package com.example.terraformmarsresourceapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.terraformmarsresourceapp.data.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(player: PlayerEntity): Long

    @Update
    suspend fun update(player: PlayerEntity)

    @Delete
    suspend fun delete(player: PlayerEntity)

    @Query("SELECT * FROM players WHERE id = :id")
    fun getPlayer(id: String): Flow<PlayerEntity?>

    @Query("SELECT * FROM players WHERE gameSessionId = :gameSessionId ORDER BY name")
    fun getPlayersBySession(gameSessionId: String): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE gameSessionId = :gameSessionId AND isActive = 1")
    fun getActivePlayer(gameSessionId: String): Flow<PlayerEntity?>

    @Query("UPDATE players SET isActive = 0 WHERE gameSessionId = :gameSessionId")
    suspend fun deactivateAllPlayers(gameSessionId: String)

    @Query("UPDATE players SET isActive = 1 WHERE id = :playerId")
    suspend fun setActivePlayer(playerId: String)

    @Query("DELETE FROM players WHERE gameSessionId = :gameSessionId")
    suspend fun deletePlayersBySession(gameSessionId: String)
}

