package com.obss.firstapp.data.model.review

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("username") val username: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("avatar_path") val avatarPath: String?,
)
