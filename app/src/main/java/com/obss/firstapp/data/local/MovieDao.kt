package com.obss.firstapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movie ORDER BY id DESC")
    suspend fun getAllFavorites(): List<FavoriteMovie>

    @Query("SELECT * FROM favorite_movie WHERE movie_id = :movieId")
    suspend fun getMovieById(movieId: Int): FavoriteMovie?

    @Insert
    suspend fun insertMovie(movie: FavoriteMovie)

    @Delete
    suspend fun deleteMovie(movie: FavoriteMovie)
}
