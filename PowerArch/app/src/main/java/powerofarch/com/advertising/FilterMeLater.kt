package powerofarch.com.advertising

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import powerofarch.com.ApppppCL.Companion.C1
import powerofarch.com.ApppppCL.Companion.jsoupCheck
import powerofarch.com.ApppppCL.Companion.linkFilterPart1
import powerofarch.com.ApppppCL.Companion.linkFilterPart2
import powerofarch.com.ApppppCL.Companion.odone
import kotlinx.coroutines.*
import powerofarch.com.R
import powerofarch.com.game.view.SplashActivity
import java.net.HttpURLConnection
import java.net.URL

class FilterMeLater : AppCompatActivity() {

    lateinit var jsoup: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_me_later)

        jsoup = ""
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
        val hawk: String? = sharPref.getString(C1, "null")
        val forJsoupSetNaming =
            "${linkFilterPart1}${linkFilterPart2}${odone}$hawk"

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