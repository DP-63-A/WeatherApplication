package com.example.weatherapplication.di

import android.content.Context
import com.example.weatherapplication.R
import com.example.weatherapplication.data.preferences.DataStoreManager
import com.example.weatherapplication.data.preferences.UserPreferencesImpl
import com.example.weatherapplication.data.remote.WeatherApi
import com.example.weatherapplication.data.repository.WeatherRepositoryImpl
import com.example.weatherapplication.domain.preferences.UserPreferences
import com.example.weatherapplication.domain.repository.WeatherRepository
import com.example.weatherapplication.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiKey(@ApplicationContext context: Context): String =
        context.getString(R.string.open_weather_api_key)

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi =
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        apiKey: String
    ): WeatherRepository = WeatherRepositoryImpl(api, apiKey)

    @Provides
    fun provideGetWeatherUseCase(repo: WeatherRepository) =
        GetWeatherUseCase(repo)

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(dataStoreManager: DataStoreManager): UserPreferences {
        return UserPreferencesImpl(dataStoreManager)
    }

}
