package com.obss.firstapp.data.model.movieDetail

import com.google.gson.annotations.SerializedName

data class MoviePoster(
    @SerializedName("file_path") val filePath: String?,
)
