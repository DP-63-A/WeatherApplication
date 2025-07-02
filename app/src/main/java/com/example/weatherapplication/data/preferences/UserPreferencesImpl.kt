package com.example.weatherapplication.data.preferences

import com.example.weatherapplication.domain.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : UserPreferences {
    override suspend fun saveLastCity(city: String) {
        dataStoreManager.saveLastCity(city)
    }

    override fun getLastCity(): Flow<String?> {
        return dataStoreManager.getLastCity()
    }

    override val recentCitiesFlow: Flow<List<String>> = dataStoreManager.recentCitiesFlow

    override suspend fun saveCity(city: String) {
        dataStoreManager.saveCity(city)
    }
}
