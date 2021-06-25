package com.mobiweb.challenge.weatherforecast.models.currentWeatherDataModels


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)