package com.obss.firstapp.model.movie

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("results") val results: List<Movie>?,
)
