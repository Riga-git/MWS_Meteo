package com.lri.projetmeteo

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lri.currentweather.ApiCurrentWeatherData
import com.lri.forecastweather.ApiForecastWeatherData
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val API_ID = "b51bcf76e464f2d749868e7a73e746f3"

interface CurrentWeatherApiService {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") location: String
    ): Deferred<ApiCurrentWeatherData> /* Deferred -> coroutine */

    companion object {
        operator fun invoke(): CurrentWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addEncodedQueryParameter(
                        "appid", API_ID
                    ) // add constant values with interceptor
                    .addEncodedQueryParameter("units", "metric")
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrentWeatherApiService::class.java)
        }
    }
}


interface ForecastWeatherApiService {
    @GET("onecall")
    fun getForecastWeather(
            @Query("lat") lat: String,
            @Query("lon") lon: String
    ): Deferred<ApiForecastWeatherData> /* Deferred -> coroutine */

    companion object {
        operator fun invoke(): ForecastWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addEncodedQueryParameter(
                                "appid", API_ID
                        ) // add constant values with interceptor
                        .addEncodedQueryParameter("units", "metric")
                        .addEncodedQueryParameter("exclude","current,minutely,hourly,alerts")
                        .build()
                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ForecastWeatherApiService::class.java)
        }
    }
}