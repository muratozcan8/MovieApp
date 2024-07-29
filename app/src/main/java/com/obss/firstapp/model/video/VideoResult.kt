package com.obss.firstapp.model.video

import com.google.gson.annotations.SerializedName

data class VideoResult(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String,
    @SerializedName("size") val size: Int

)
