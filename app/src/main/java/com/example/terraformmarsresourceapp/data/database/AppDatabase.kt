package com.example.terraformmarsresourceapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.terraformmarsresourceapp.data.dao.GameSessionDao
import com.example.terraformmarsresourceapp.data.dao.PlayerDao
import com.example.terraformmarsresourceapp.data.model.GameSessionEntity
import com.example.terraformmarsresourceapp.data.model.PlayerEntity

@Database(
    entities = [GameSessionEntity::class, PlayerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun playerDao(): PlayerDao
}

