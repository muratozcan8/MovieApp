package com.obss.firstapp.data.model.movieSearch

import com.google.gson.annotations.SerializedName

data class MovieSearchResult(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieSearch>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
)
