package com.example.weatherapplication.domain.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    suspend fun saveLastCity(city: String)
    fun getLastCity(): Flow<String?>
}
