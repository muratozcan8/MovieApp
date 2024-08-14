package com.obss.firstapp.data.model.movieDetail

import com.google.gson.annotations.SerializedName

data class MovieImage(
    @SerializedName("id") val id: Int,
    @SerializedName("backdrops") val backdrops: List<MoviePoster>?,
)
