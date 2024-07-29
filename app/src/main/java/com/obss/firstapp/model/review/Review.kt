package com.obss.firstapp.model.review

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<ReviewResult>,
)
