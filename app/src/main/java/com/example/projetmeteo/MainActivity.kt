package com.example.projetmeteo

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary.Words.APP_ID
import android.view.View
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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

    inner class Download : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg p0: String?): String? {

            var result = ""
            var url: URL
            val httpURLConnection: HttpURLConnection

            try {
                // Get api infos
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                // update vue with whether info
            } catch (e: Exception) {

                e.printStackTrace()

            }
        }
    }


}