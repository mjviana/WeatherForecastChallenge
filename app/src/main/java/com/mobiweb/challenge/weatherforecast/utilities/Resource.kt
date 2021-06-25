package com.mobiweb.challenge.weatherforecast.utilities

/**
 * Class to handle results from API
 * More info at: https://developer.android.com/kotlin/coroutines?gclsrc=aw.ds&gclid=Cj0KCQjw2tCGBhCLARIsABJGmZ7RL7LCxhkZSuobwJ_b2467cutnvnxwLZrl1IvlPmizIyUz28imBRwaAiVLEALw_wcB#kts
 */
sealed class Resource<T>(
    val data: T? = null,
    var message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}