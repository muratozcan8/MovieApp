package com.obss.firstapp.data.model.video

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<VideoResult>,
)
