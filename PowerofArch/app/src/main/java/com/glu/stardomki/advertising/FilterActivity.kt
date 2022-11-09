package com.glu.stardomki.advertising

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.glu.stardomki.AppClass.Companion.DONE
import com.glu.stardomki.AppClass.Companion.FILTER_URL_1
import com.glu.stardomki.AppClass.Companion.FILTER_URL_2
import com.glu.stardomki.AppClass.Companion.HW_C1
import com.glu.stardomki.AppClass.Companion.jsoupCheck
import com.glu.stardomki.R
import kotlinx.coroutines.*
import com.glu.stardomki.game.view.SplashActivity
import java.net.HttpURLConnection
import java.net.URL

class FilterActivity : AppCompatActivity() {

    private var jsoup: String = ""

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_me_later)

        val job = GlobalScope.launch(Dispatchers.IO) {
            jsoup = coroutineTask()
            Log.d("jsoup status from global scope", jsoup)
        }

        runBlocking {
            try {
                job.join()
                Log.d("jsoup status out of global scope", jsoup)

                if (jsoup == jsoupCheck) {
                    Intent(applicationContext, SplashActivity::class.java).also { startActivity(it) }
                } else {
                    Intent(applicationContext, Web::class.java).also { startActivity(it) }
                }
                finish()
            } catch (e: Exception) {

            }
        }

    }

    private suspend fun coroutineTask(): String {
        val sharPref = getSharedPreferences("SP", MODE_PRIVATE)
        val hawk: String? = sharPref.getString(HW_C1, "null")
        val forJsoupSetNaming = "${FILTER_URL_1}${FILTER_URL_2}${DONE}$hawk"

        withContext(Dispatchers.IO) {
                getCodeFromUrl(forJsoupSetNaming)
                Log.d("Check1C", forJsoupSetNaming)
        }
        return jsoup
    }

    private fun getCodeFromUrl(link: String) {
        val url = URL(link)
        val urlConnection = url.openConnection() as HttpURLConnection

        try {
            val text = urlConnection.inputStream.bufferedReader().readText()

            if (text.isNotEmpty()) {
                Log.d("jsoup status inside Url function", text)
                jsoup = text
            } else {
                Log.d("jsoup status inside Url function", "is null")
            }

        } catch (ex: Exception) {

        } finally {
            urlConnection.disconnect()
        }
    }
}