package com.obss.firstapp.di

import android.content.Context
import androidx.room.Room
import com.obss.firstapp.room.FavoriteMovieDatabase
import com.obss.firstapp.room.MovieDao
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
    fun provideMovieDatabase(@ApplicationContext context: Context): FavoriteMovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FavoriteMovieDatabase::class.java,
            "favorite_movie_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(movieDatabase: FavoriteMovieDatabase) : MovieDao {
        return movieDatabase.favoriteMovieDao()
    }

}