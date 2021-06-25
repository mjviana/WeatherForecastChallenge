package com.mobiweb.challenge.weatherforecast.apiService

import com.mobiweb.challenge.weatherforecast.models.currentWeatherDataModels.CurrentWeatherDataApiResponse
import com.mobiweb.challenge.weatherforecast.models.weatherForecastModels.WeatherForecastApiResponse
import com.mobiweb.challenge.weatherforecast.utilities.Constants.Companion.API_KEY
import com.mobiweb.challenge.weatherforecast.utilities.Constants.Companion.CITY_NAME
import com.mobiweb.challenge.weatherforecast.utilities.Constants.Companion.UNITS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastApi {

    /**
     * Retrieves the current weather data for the city in CITY_NAME constant value
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherData(
    @Query("q")
    cityName: String = CITY_NAME,
    @Query("units")
    units: String = UNITS,
    @Query("appid")
    apiKey: String = API_KEY
    ): Response<CurrentWeatherDataApiResponse>

    /**
     * Retrieves the weather forecast for the city in CITY_NAME constant value
     */
    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("q")
        cityName: String = CITY_NAME,
        @Query("units")
        units: String = UNITS,
        @Query("appid")
        apiKey: String = API_KEY
    ): Response<WeatherForecastApiResponse>
}