package com.obss.firstapp.model.actor

import com.google.gson.annotations.SerializedName
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.credit.Crew

data class ActorMovies(
    @SerializedName("cast") val cast: List<Cast>?,
    @SerializedName("crew") val crew: List<Crew>?,
    @SerializedName("id") val id: Int
)
