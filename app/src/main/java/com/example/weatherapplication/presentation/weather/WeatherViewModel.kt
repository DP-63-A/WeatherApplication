package com.example.weatherapplication.presentation.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    var uiState by mutableStateOf(WeatherUiState())
        private set

    init {
        loadWeather()
    }

    private fun loadWeather() {
        viewModelScope.launch {
            val weather = getWeatherUseCase("Vilnius")
            uiState = WeatherUiState(
                city = weather.city,
                temperature = "${weather.temperature}°C",
                description = weather.description,
                isLoading = false
            )
        }
    }
}
