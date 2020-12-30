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
        return apiService.getCurrentWeather(location = "London").await()
    }

    private suspend fun setCurrentDataOnMainThread(currentWeatherData : ApiCurrentWeatherData){
        withContext(Main){
            //CurrentDayWhetherDescription.text = currentWeatherData.weather[1].description
            CurrentDayTemp.text = "Temp" + currentWeatherData.main.temp.toString()
            CurrentDayHumidity.text = currentWeatherData.main.humidity.toString()
            CurrentDayRain.text = currentWeatherData.rain.h3.toString()
            CurrentDayPressure.text = currentWeatherData.main.pressure.toString()
        }
    }
}