package com.obss.firstapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movie")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "movie_id")
    var movieId: Int?,
    @ColumnInfo(name = "movie_title")
    var title: String?,
    @ColumnInfo(name = "movie_poster_path")
    var posterPath: String?,
    @ColumnInfo(name = "average_vote")
    var averageVote: Double?,
)
