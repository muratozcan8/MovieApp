package com.obss.firstapp.model.review

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("username") val username: String,
    @SerializedName("rating") val rating: Int,
)
