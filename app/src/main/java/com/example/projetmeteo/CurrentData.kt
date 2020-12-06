package com.example.projetmeteo

import com.squareup.moshi.Json

    data class CurrentData( val description: String,
                            val temperature : Float, // °C
                            val rain : Float, //  Rain volume for the last 3 hours, mm
                            val pressure : Int, // hPa
                            val humidity : Int, // %
                            val iconId : String

    ) {}

    /*data class ForecastDataDay( val iconId: String,
                                val dt : Int // time Unix UTC
    ) {}

    fun fetchWhether(){
        println("Whether fetched")
    }
}*/