package com.rainbowt0506.philipplackner_weatherapp.presentation

import com.rainbowt0506.philipplackner_weatherapp.domain.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
