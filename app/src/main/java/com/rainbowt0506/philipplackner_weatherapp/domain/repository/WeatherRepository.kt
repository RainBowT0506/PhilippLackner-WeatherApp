package com.rainbowt0506.philipplackner_weatherapp.domain.repository

import com.rainbowt0506.philipplackner_weatherapp.domain.util.Resource
import com.rainbowt0506.philipplackner_weatherapp.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}