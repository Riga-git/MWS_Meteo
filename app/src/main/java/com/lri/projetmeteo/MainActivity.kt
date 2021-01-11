package com.lri.projetmeteo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.lri.currentweather.ApiCurrentWeatherData
import com.lri.forecastweather.ApiForecastWeatherData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    fun getWhetherData(view: View) {
        CoroutineScope(IO).launch { apiRequests() }
    }

    private suspend fun apiRequests() {
        val currentWeatherData = getWhetherDataFromApi(City.text.toString())
        val forecastWeatherData = getForecastDataFromApi(
            currentWeatherData.coord.lat.toString(),
            currentWeatherData.coord.lon.toString()
        )
        setCurrentDataOnMainThread(currentWeatherData, forecastWeatherData)
        setIcons(currentWeatherData, forecastWeatherData)
    }

    private suspend fun getWhetherDataFromApi(city: String): ApiCurrentWeatherData {
        val apiService = CurrentWeatherApiService()
        var response = ApiCurrentWeatherData()
        try {
            response = apiService.getCurrentWeather(location = city).await()
        } finally {
            return response
        }
    }

    private suspend fun getForecastDataFromApi(lat: String, lon: String): ApiForecastWeatherData {
        val apiService = ForecastWeatherApiService()
        var response = ApiForecastWeatherData()
        try {
            response = apiService.getForecastWeather(lat = lat, lon = lon).await()
        } finally {
            return response
        }
    }

    private suspend fun setCurrentDataOnMainThread(
        currentWeatherData: ApiCurrentWeatherData,
        forecastWeatherData: ApiForecastWeatherData
    ) {
        withContext(Main) {
            CurrentDayWhetherDescription.text =
                currentWeatherData.weather.first().description.toUpperCase()
            CurrentDayTemp.text = "Temp : " + currentWeatherData.main.temp.toString() + " °C"
            CurrentDayHumidity.text =
                "Humidity : " + currentWeatherData.main.humidity.toString() + " %"
            CurrentDayRain.text =
                "Rain(3h) : " + if (currentWeatherData.rain != null) currentWeatherData.rain.h3.toString() else {
                    "0"
                } + " mm/h"
            CurrentDayPressure.text =
                "Pressure : " + currentWeatherData.main.pressure.toString() + " hPa"
            CurrentDay.text = "Meteo, ${currentWeatherData.name}"

            if (forecastWeatherData.daily != null) {
                D1Date.text = dateFormatter(forecastWeatherData.daily[1].dt)
                D1TempMax.text = forecastWeatherData.daily[1].temp.max.toString() + "°C"
                D1TempMin.text = forecastWeatherData.daily[1].temp.min.toString() + "°C"
                D2Date.text = dateFormatter(forecastWeatherData.daily[2].dt)
                D2TempMax.text = forecastWeatherData.daily[2].temp.max.toString() + "°C"
                D2TempMin.text = forecastWeatherData.daily[2].temp.min.toString() + "°C"
                D3Date.text = dateFormatter(forecastWeatherData.daily[3].dt)
                D3TempMax.text = forecastWeatherData.daily[3].temp.max.toString() + "°C"
                D3TempMin.text = forecastWeatherData.daily[3].temp.min.toString() + "°C"
                D4Date.text = dateFormatter(forecastWeatherData.daily[4].dt)
                D4TempMax.text = forecastWeatherData.daily[4].temp.max.toString() + "°C"
                D4TempMin.text = forecastWeatherData.daily[4].temp.min.toString() + "°C"
            }
        }
    }

    private suspend fun setIcons(
        currentWeatherData: ApiCurrentWeatherData,
        forecastWeatherData: ApiForecastWeatherData
    ) {
        withContext(Main) {
            // main icons
            Picasso.get()
                .load("http://openweathermap.org/img/wn/${currentWeatherData.weather.first().icon}@2x.png")
                .into(CurrentDayWhetherIcon)
            // forecast icons
            Picasso.get()
                .load("http://openweathermap.org/img/wn/${forecastWeatherData.daily[0].weather.first().icon}@2x.png")
                .into(D1WhetherIcon)
            Picasso.get()
                .load("http://openweathermap.org/img/wn/${forecastWeatherData.daily[1].weather.first().icon}@2x.png")
                .into(D2WhetherIcon)
            Picasso.get()
                .load("http://openweathermap.org/img/wn/${forecastWeatherData.daily[2].weather.first().icon}@2x.png")
                .into(D3WhetherIcon)
            Picasso.get()
                .load("http://openweathermap.org/img/wn/${forecastWeatherData.daily[3].weather.first().icon}@2x.png")
                .into(D4WhetherIcon)
        }
    }

    private fun dateFormatter(timeStamp: Long): String {
        val date = DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(timeStamp))
        return date.substring(8,10) + "/" + date.substring(5,7)
    }


    // LOCATION

    @Override
    fun checkPermission():Boolean{
        return ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            1010
        )
    }

    fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getLastLocation(view: View) {
        requestPermission()
        if (checkPermission()) {
            if (isLocationEnabled()) {
              newLocationData()
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "No permission for Location service", Toast.LENGTH_SHORT).show()
        }
    }

    @Override
    fun checkSelfPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun newLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkSelfPermission()){
            fusedLocationProviderClient!!.requestLocationUpdates(
                    locationRequest,locationCallback,Looper.myLooper())
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            getCityName(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun getCityName(lat: Double,long: Double){
        var cityName:String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var adress = geoCoder.getFromLocation(lat,long,1)
        cityName = adress.get(0).locality
        City.setText(cityName)
    }
}