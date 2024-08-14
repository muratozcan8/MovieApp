package com.obss.firstapp.data.model.review

import com.google.gson.annotations.SerializedName

data class ReviewResult(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("id") val id: String,
    @SerializedName("author_details") val authorDetails: AuthorDetails,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_more") var isMore: Boolean = false,
)
