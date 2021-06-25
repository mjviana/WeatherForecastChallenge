package com.mobiweb.challenge.weatherforecast.models.weatherForecastModels


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String
)