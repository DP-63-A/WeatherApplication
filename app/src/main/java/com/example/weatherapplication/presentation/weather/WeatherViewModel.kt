/*package com.example.weatherapplication.presentation.weather

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
}*/


package com.example.weatherapplication.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import java.io.IOException

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeather: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun onCityInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(cityInput = input)
    }

    fun loadWeather() {
        val city = _uiState.value.cityInput.takeIf { it.isNotBlank() } ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val data = getWeather(city)
                _uiState.value = WeatherUiState(
                    cityInput = city,
                    city = data.city,
                    temperature = "${data.temperature}°C",
                    description = data.description,
                    isLoading = false
                )
            } catch (e: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Network error"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Oops, something went wrong"
                )
            }
        }
    }
}
