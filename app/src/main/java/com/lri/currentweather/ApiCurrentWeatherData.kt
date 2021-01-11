package com.lri.currentweather


data class ApiCurrentWeatherData(
    val base: String = "-",
    val clouds: Clouds = Clouds(),
    val cod: Int = 0,
    val coord: Coord = Coord(),
    val dt: Int = 0,
    val id: Int = 0,
    val main: Main = Main(),
    val name: String = "Location not found",
    val rain: Rain = Rain(),
    val snow: Snow = Snow(),
    val sys: Sys = Sys(),
    val timezone: Int = 0,
    val visibility: Int = 0,
    val weather: List<Weather> = listOf<Weather>(Weather()),
    val wind: Wind = Wind()
)