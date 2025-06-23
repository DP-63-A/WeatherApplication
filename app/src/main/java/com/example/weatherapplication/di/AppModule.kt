/*package com.example.weatherapplication.di

import com.example.weatherapplication.domain.repository.WeatherRepository
import com.example.weatherapplication.domain.repository.WeatherRepositoryImpl
import com.example.weatherapplication.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepositoryImpl()
    }

    @Provides
    fun provideGetWeatherUseCase(
        repo: WeatherRepository
    ): GetWeatherUseCase = GetWeatherUseCase(repo)
}*/



package com.example.weatherapplication.di

import android.content.Context
import com.example.weatherapplication.R
import com.example.weatherapplication.data.remote.WeatherApi
import com.example.weatherapplication.data.repository.WeatherRepositoryImpl
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
}
