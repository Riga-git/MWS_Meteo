package com.example.projetmeteo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getPosition(view: View) {

    }
    fun getWhetherData(view: View){
        getWhetherDataFromApi()
    }

    suspend fun getWhetherDataFromApi() {
        try{
            val listResult = WhetherApi.retrofitService.getProperties()
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }
}