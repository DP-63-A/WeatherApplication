package com.example.weatherapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapplication.presentation.weather.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = state.cityInput,
            onValueChange = viewModel::onCityInputChanged,
            label = { Text("City") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = viewModel::loadWeather,
            enabled = !state.isLoading
        ) {
            Text("Get Weather")
        }
        Spacer(Modifier.height(16.dp))
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
        } else if (state.city.isNotEmpty()) {
            Text(state.city, style = MaterialTheme.typography.headlineMedium)
            Text(state.temperature, style = MaterialTheme.typography.headlineLarge)
            Text(state.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
