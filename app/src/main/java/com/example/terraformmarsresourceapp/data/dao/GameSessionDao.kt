package com.example.terraformmarsresourceapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.terraformmarsresourceapp.data.model.GameSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSessionDao {
    @Insert
    suspend fun insert(gameSession: GameSessionEntity): Long

    @Update
    suspend fun update(gameSession: GameSessionEntity)

    @Delete
    suspend fun delete(gameSession: GameSessionEntity)

    @Query("SELECT * FROM game_sessions WHERE id = :id")
    fun getSession(id: String): Flow<GameSessionEntity?>

    @Query("SELECT * FROM game_sessions ORDER BY lastModified DESC")
    fun getAllSessions(): Flow<List<GameSessionEntity>>

    @Query("DELETE FROM game_sessions WHERE id = :id")
    suspend fun deleteById(id: String)
}

