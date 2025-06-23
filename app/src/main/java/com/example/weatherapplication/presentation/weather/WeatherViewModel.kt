package com.example.weatherapplication.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.domain.usecase.GetWeatherUseCase
import com.example.weatherapplication.domain.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import java.io.IOException

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeather: GetWeatherUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        viewModelScope.launch {
            userPreferences.getLastCity().collect { savedCity ->
                savedCity?.let {
                    _uiState.value = _uiState.value.copy(cityInput = it)
                    loadWeather(it)
                }
            }
        }
    }

    fun onCityInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(cityInput = input)
    }

    fun loadWeather(cityOverride: String? = null) {
        val city = cityOverride ?: _uiState.value.cityInput.takeIf { it.isNotBlank() } ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val data = getWeather(city)
                _uiState.value = _uiState.value.copy(
                    city = data.city,
                    temperature = "${data.temperature}°C",
                    description = data.description,
                    isLoading = false
                )
                userPreferences.saveLastCity(city)
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