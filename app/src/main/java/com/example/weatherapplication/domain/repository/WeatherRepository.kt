/*package com.example.weatherapplication.domain.repository

import com.example.weatherapplication.domain.model.WeatherData

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherData
}*/



package com.example.weatherapplication.domain.repository

import com.example.weatherapplication.domain.model.WeatherData

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherData
}
