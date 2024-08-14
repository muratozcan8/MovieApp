package com.obss.firstapp.data.model.movieSearch

import com.google.gson.annotations.SerializedName

data class MovieSearch(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("isFavorite") var isFavorite: Boolean = false,
)
