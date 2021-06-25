package com.mobiweb.challenge.weatherforecast.apiService

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.mobiweb.challenge.weatherforecast.utilities.Constants.Companion.API_BASE_URL
import com.mobiweb.challenge.weatherforecast.utilities.LocalDateTimeConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class WeatherForecastApiClient {
    private lateinit var weatherForecastApi: WeatherForecastApi

    /**
     * Sets and retrieves the instance of the weather api
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getApiService(): WeatherForecastApi {

        // Initialize ApiService if not initialized yet
        if (!::weatherForecastApi.isInitialized) {

            // Configure client to make http requests and get responses
            val okHttpClientBuilder = OkHttpClient.Builder()

            // Configure logging for requests and responses
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(logging)

            // Handles the conversion of string to LocalDatetime in Json
            val dateGsonBuilder =
                GsonBuilder().registerTypeAdapter(
                    LocalDateTime::class.java,
                    LocalDateTimeConverter()
                ).create()

            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(dateGsonBuilder))
                .client(okHttpClientBuilder.build())
                .build()

            weatherForecastApi = retrofit.create(WeatherForecastApi::class.java)
        }
        return weatherForecastApi
    }
}