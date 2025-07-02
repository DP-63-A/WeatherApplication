package com.example.weatherapplication.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.data.preferences.DataStoreManager
import com.example.weatherapplication.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        viewModelScope.launch {
            dataStoreManager.recentCitiesFlow.collectLatest { cities ->
                _uiState.value = _uiState.value.copy(recentCities = cities)
            }
        }
    }

    fun onCityInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(cityInput = input)
    }

    fun loadWeather() {
        val city = _uiState.value.cityInput.trim()
        if (city.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = runCatching { getWeatherUseCase(city) }

            _uiState.value = _uiState.value.copy(isLoading = false)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    city = it.city,
                    temperature = it.temperature.toString(),
                    description = it.description,
                    error = null
                )
                dataStoreManager.saveCity(it.city)
            }.onFailure {
                _uiState.value = _uiState.value.copy(error = it.message)
            }
        }

    }

    fun onRecentCitySelected(city: String) {
        onCityInputChanged(city)
        loadWeather()
    }
}