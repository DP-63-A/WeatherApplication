package com.example.weatherapplication.presentation.weather

data class WeatherUiState(
    val cityInput: String = "",
    val city: String = "",
    val temperature: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentCities: List<String> = emptyList()
)
