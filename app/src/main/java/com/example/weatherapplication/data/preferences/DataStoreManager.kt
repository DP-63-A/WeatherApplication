package com.example.weatherapplication.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

class DataStoreManager(private val context: Context) {

    private val RECENT_CITIES_KEY = stringPreferencesKey("recent_cities")

    val recentCitiesFlow: Flow<List<String>> = context.dataStore.data.map { preferences ->
        preferences[RECENT_CITIES_KEY]?.let {
            runCatching { Json.decodeFromString<List<String>>(it) }.getOrDefault(emptyList())
        } ?: emptyList()
    }

    suspend fun saveCity(city: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[RECENT_CITIES_KEY]?.let {
                runCatching { Json.decodeFromString<List<String>>(it) }.getOrDefault(emptyList())
            } ?: emptyList()
            val updated = listOf(city) + current.filterNot { it.equals(city, true) }
            preferences[RECENT_CITIES_KEY] = Json.encodeToString(updated.take(5))
        }
    }

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