package com.mobiweb.challenge.weatherforecast.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.mobiweb.challenge.weatherforecast.apiService.WeatherForecastApiClient

class WeatherForecastRepository {

    /**
     * Get the data weather for the current day
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCurrentWeatherData() =
        WeatherForecastApiClient().getApiService().getCurrentWeatherData()

    /**
     * Gets the weather forecast for the next five days
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getWeatherForecast() =
        WeatherForecastApiClient().getApiService().getWeatherForecast()
}