package com.rainbowt0506.philipplackner_weatherapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.rainbowt0506.philipplackner_weatherapp.data.mappers.toWeatherInfo
import com.rainbowt0506.philipplackner_weatherapp.data.remote.WeatherApi
import com.rainbowt0506.philipplackner_weatherapp.domain.repository.WeatherRepository
import com.rainbowt0506.philipplackner_weatherapp.domain.util.Resource
import com.rainbowt0506.philipplackner_weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(data = api.getWeatherData(lat, long).toWeatherInfo())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}