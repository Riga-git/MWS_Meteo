package com.example.projetmeteo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import com.example.projetmeteo.network.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
    }

    fun getPosition(view: View) {
    }

    fun getWhetherData(view: View){
        CoroutineScope(IO).launch { apiRequest() }
    }
    private suspend fun apiRequest(){
        val result = getWhetherDataFromApi()
        setCurrentDataOnMainThread(result)
    }

    private suspend fun getWhetherDataFromApi() : ApiCurrentWeatherData {
        val apiService = WeatherApiService()
        var response = ApiCurrentWeatherData()
        try {
            response = apiService.getCurrentWeather(location = City.text.toString()).await()
        }finally {
            return response
        }
    }

    private suspend fun setCurrentDataOnMainThread(currentWeatherData : ApiCurrentWeatherData){
        withContext(Main){
            CurrentDayWhetherDescription.text = currentWeatherData.weather.first().description.toUpperCase()
            CurrentDayTemp.text = "Temp : " + currentWeatherData.main.temp.toString() + " °C"
            CurrentDayHumidity.text = "Humidity : " + currentWeatherData.main.humidity.toString() + " %"
            CurrentDayRain.text = "Rain(3h) : " + if(currentWeatherData.rain != null) currentWeatherData.rain.h3.toString() else {"0"} + " mm/h"
            CurrentDayPressure.text = "Pressure : " + currentWeatherData.main.pressure.toString() + " hPa"
            CurrentDay.text = "Meteo, ${currentWeatherData.name}"
        }
    }
}