package com.lri.forecastweather

data class ApiForecastWeatherData(
    val daily: List<Daily> = listOf(Daily()),
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String = "-",
    val timezone_offset: Int = 0
)