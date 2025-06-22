package com.example.weatherapplication.presentation.weather

data class WeatherUiState(
    val city: String = "Vilnius",
    val temperature: String = "--°C",
    val description: String = "Loading...",
    val isLoading: Boolean = true
)
