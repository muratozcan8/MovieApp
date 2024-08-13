package com.obss.firstapp.model.movie

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<Movie>,
)
