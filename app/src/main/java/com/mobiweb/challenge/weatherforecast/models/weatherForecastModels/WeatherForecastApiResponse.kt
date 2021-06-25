package com.mobiweb.challenge.weatherforecast.models.weatherForecastModels


import com.google.gson.annotations.SerializedName

data class WeatherForecastApiResponse(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val numberOfTimeStamps: Int,
    @SerializedName("cod")
    val statusCode: String,
    @SerializedName("list")
    val details: List<Details>,
    @SerializedName("message")
    val message: Int
)