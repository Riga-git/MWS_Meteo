package com.example.projetmeteo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary.Words.APP_ID
import android.view.View
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getPosition(view: View) {
        println(BuildConfig.API_ID)

    }


    fun getWhetherData(view: View) {
        println("get Whether data")
    }

    /*val currentWeather: LiveData<String> = liveData {
        emit(LOADING_STRING)
        emitSource(dataSource.fetchWeather())
    }*/
}