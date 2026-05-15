package com.example.terraformmarsresourceapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity(tableName = "game_sessions")
@Serializable
data class GameSessionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Game Session",
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val generationCount: Int = 0
)

@Entity(tableName = "players")
@Serializable
data class PlayerEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val gameSessionId: String,
    val name: String,
    val isActive: Boolean = false,
    val terraformingRating: Int = 0,
    @Embedded
    val resources: ResourceStateEntity = ResourceStateEntity()
)

@Serializable
data class ResourceStateEntity(
    val megacredits: Int = 0,
    val steel: Int = 0,
    val titanium: Int = 0,
    val plants: Int = 0,
    val energy: Int = 0,
    val heat: Int = 0,
    val megacreditsProduction: Int = 0,
    val steelProduction: Int = 0,
    val titaniumProduction: Int = 0,
    val plantsProduction: Int = 0,
    val energyProduction: Int = 0,
    val heatProduction: Int = 0
)

// Business logic models
data class GameSession(
    val id: String,
    val name: String,
    val createdAt: Long,
    val lastModified: Long,
    val generationCount: Int,
    val players: List<Player>
)

data class Player(
    val id: String,
    val name: String,
    val terraformingRating: Int,
    val resources: ResourceState,
    val isActive: Boolean
)

data class ResourceState(
    val megacredits: Int = 0,
    val steel: Int = 0,
    val titanium: Int = 0,
    val plants: Int = 0,
    val energy: Int = 0,
    val heat: Int = 0,
    val megacreditsProduction: Int = 0,
    val steelProduction: Int = 0,
    val titaniumProduction: Int = 0,
    val plantsProduction: Int = 0,
    val energyProduction: Int = 0,
    val heatProduction: Int = 0
)

