package com.example.weatherapplication.domain.repository

import com.example.weatherapplication.domain.model.WeatherData
import kotlinx.coroutines.delay
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(): WeatherRepository {
    override suspend fun getWeather(city: String): WeatherData {
        delay(1000) // имитируем задержку
        return WeatherData(city = city, temperature = 21, description = "Partly Cloudy")
    }
}
