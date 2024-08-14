package com.obss.firstapp.data.model.video

import com.google.gson.annotations.SerializedName

data class VideoResult(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String?,
    @SerializedName("site") val site: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("size") val size: Int?,
    @SerializedName("official") val official: Boolean?,
)
