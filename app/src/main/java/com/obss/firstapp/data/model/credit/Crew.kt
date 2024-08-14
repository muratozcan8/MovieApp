package com.obss.firstapp.data.model.credit

import com.google.gson.annotations.SerializedName

data class Crew(
    @SerializedName("id") val id: Int?,
    @SerializedName("job") val job: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
)
