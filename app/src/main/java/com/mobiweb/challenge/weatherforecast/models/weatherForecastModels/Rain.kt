package com.mobiweb.challenge.weatherforecast.models.weatherForecastModels


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)