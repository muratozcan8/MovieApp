package com.obss.firstapp.model.movieDetail

import com.google.gson.annotations.SerializedName

data class MovieImage(
    @SerializedName("id") val id: Int,
    @SerializedName("posters") val posters: List<MoviePoster>?,
)
