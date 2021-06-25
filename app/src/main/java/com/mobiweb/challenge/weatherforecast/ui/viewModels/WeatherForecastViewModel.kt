package com.mobiweb.challenge.weatherforecast.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiweb.challenge.weatherforecast.models.currentWeatherDataModels.CurrentWeatherDataApiResponse
import com.mobiweb.challenge.weatherforecast.models.weatherForecastModels.WeatherForecastApiResponse
import com.mobiweb.challenge.weatherforecast.repository.WeatherForecastRepository
import com.mobiweb.challenge.weatherforecast.utilities.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherForecastViewModel : ViewModel() {
    private var repository: WeatherForecastRepository = WeatherForecastRepository()

    val currentWeatherData: MutableLiveData<Resource<CurrentWeatherDataApiResponse>> =
        MutableLiveData()
    val weatherForecast: MutableLiveData<Resource<WeatherForecastApiResponse>> = MutableLiveData()

    init {
        getCurrentWeatherData()
        getWeatherForecast()
    }

    private fun getCurrentWeatherData() = viewModelScope.launch {
        currentWeatherData.postValue(Resource.Loading())
        val response = repository.getCurrentWeatherData()
        currentWeatherData.postValue(handleGetCurrentDataResponse(response))

    }

    private fun getWeatherForecast() = viewModelScope.launch {
        weatherForecast.postValue(Resource.Loading())
        val response = repository.getWeatherForecast()
        weatherForecast.postValue(handleGetWeatherForecastResponse(response))
    }

    private fun handleGetCurrentDataResponse(response: Response<CurrentWeatherDataApiResponse>): Resource<CurrentWeatherDataApiResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleGetWeatherForecastResponse(response: Response<WeatherForecastApiResponse>): Resource<WeatherForecastApiResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}