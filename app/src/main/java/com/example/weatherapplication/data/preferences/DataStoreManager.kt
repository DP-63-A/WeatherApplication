package com.example.weatherapplication.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "weather_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        val LAST_CITY = stringPreferencesKey("last_city")
    }

    suspend fun saveLastCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_CITY] = city
        }
    }

    fun getLastCity(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_CITY]
        }
    }
}
