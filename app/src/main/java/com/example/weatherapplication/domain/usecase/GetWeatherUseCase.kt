package com.example.weatherapplication.domain.usecase

import com.example.weatherapplication.domain.model.WeatherData
import com.example.weatherapplication.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): WeatherData {
        return repository.getWeather(city)
    }
}
