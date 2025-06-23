package com.example.weatherapplication.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherInfoDto>
)

data class MainDto(val temp: Float)
data class WeatherInfoDto(val description: String)
