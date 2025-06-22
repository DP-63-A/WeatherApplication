package com.example.weatherapplication.di

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
}
