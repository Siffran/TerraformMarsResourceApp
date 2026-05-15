package com.example.terraformmarsresourceapp.di

import android.content.Context
import androidx.room.Room
import com.example.terraformmarsresourceapp.data.database.AppDatabase
import com.example.terraformmarsresourceapp.data.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "terraformmars.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSessionRepository(database: AppDatabase): SessionRepository {
        return SessionRepository(
            gameSessionDao = database.gameSessionDao(),
            playerDao = database.playerDao()
        )
    }
}

