package com.example.projetmeteo

class Whether {
    private val apiUrl = "api.openweathermap.org/data/2.5/weather"

    data class CurrentData( val description: String,
                            val temperature : Float, // °C
                            val rain : Float, //  Rain volume for the last 3 hours, mm
                            val pressure : Int, // hPa
                            val humidity : Int, // %
                            val iconId : String

    ) {}

    data class ForecastDataDay( val iconId: String,
                                val dt : Int // time Unix UTC
    ) {}

    fun fetchWhether(){
        println("Whether fetched")
    }
}