package com.example.projetmeteo

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface WhetherApiService {
    /**
     * Returns a Coroutine [List] of [CurrentData] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the endpoint will be requested with the GET
     * HTTP method
     */
    @GET("?q=London,uk&units=metric&appid=b51bcf76e464f2d749868e7a73e746f3")
    suspend fun getProperties(): List<CurrentData>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object WhetherApi {
    val retrofitService : WhetherApiService by lazy { retrofit.create(WhetherApiService::class.java) }
}