package com.example.weatherapplication.data.repository

import com.example.weatherapplication.data.remote.WeatherApi
import com.example.weatherapplication.domain.model.WeatherData
import com.example.weatherapplication.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val apiKey: String
) : WeatherRepository {
    override suspend fun getWeather(city: String): WeatherData {
        val dto = api.getByCity(city, apiKey)
        val temp = dto.main.temp.toInt()
        val desc = dto.weather.firstOrNull()?.description.orEmpty()
        return WeatherData(city = dto.cityName, temperature = temp, description = desc)
    }
}
