package com.lri.forecastweather

data class Weather(
    val description: String = "-",
    val icon: String = "-",
    val id: Int = 0,
    val main: String = "-"
)