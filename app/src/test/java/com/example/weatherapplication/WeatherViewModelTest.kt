package com.example.weatherapplication

import app.cash.turbine.test
import com.example.weatherapplication.domain.model.WeatherData
import com.example.weatherapplication.domain.preferences.UserPreferences
import com.example.weatherapplication.domain.usecase.GetWeatherUseCase
import com.example.weatherapplication.presentation.weather.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private lateinit var userPreferences: UserPreferences

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getWeatherUseCase = mockk()
        userPreferences = mockk()

        every { userPreferences.getLastCity() } returns flowOf(null)

        viewModel = WeatherViewModel(getWeatherUseCase, userPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCityInputChanged updates cityInput`() = testScope.runTest {
        viewModel.onCityInputChanged("Vilnius")
        assertEquals("Vilnius", viewModel.uiState.value.cityInput)
    }

    @Test
    fun `loadWeather successfully updates state`() = testScope.runTest {
        val weather = WeatherData(
            city = "Vilnius",
            temperature = 25,
            description = "Sunny"
        )

        coEvery { getWeatherUseCase("Vilnius") } returns weather
        coEvery { userPreferences.saveLastCity("Vilnius") } just Runs

        viewModel.onCityInputChanged("Vilnius")
        viewModel.loadWeather()

        viewModel.uiState.test {

            val loading = awaitItem()
            assertEquals(true, loading.isLoading)

            val loaded = awaitItem()
            assertEquals("Vilnius", loaded.city)
            assertEquals("25°C", loaded.temperature)
            assertEquals("Sunny", loaded.description)
            assertEquals(false, loaded.isLoading)
        }
    }

    @Test
    fun `loadWeather handles IOException`() = testScope.runTest {
        coEvery { getWeatherUseCase("Minsk") } throws IOException("network failed")
        coEvery { userPreferences.saveLastCity(any()) } just Runs

        viewModel.onCityInputChanged("Minsk")
        viewModel.loadWeather()

        viewModel.uiState.test {
            val loading = awaitItem()
            assertEquals(true, loading.isLoading)

            val errorState = awaitItem()
            assertEquals("Network error", errorState.error)
            assertEquals(false, errorState.isLoading)
        }

    }

    @Test
    fun `loadWeather does nothing when city is blank`() = testScope.runTest {
        viewModel.onCityInputChanged("")
        viewModel.loadWeather()

        assertEquals("", viewModel.uiState.value.city)
        assertEquals("", viewModel.uiState.value.cityInput)
    }
}
