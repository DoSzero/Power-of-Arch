package com.glu.stardomki.advertising

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.glu.stardomki.AppClass.Companion.AF_DEV_KEY
import com.glu.stardomki.AppClass.Companion.CH
import com.glu.stardomki.AppClass.Companion.HW_C1
import com.glu.stardomki.AppClass.Companion.HW_D1
import com.glu.stardomki.AppClass.Companion.URL_APPS_1
import com.glu.stardomki.AppClass.Companion.URL_APPS_2
import com.glu.stardomki.databinding.ActivityWaiteBinding
import kotlinx.coroutines.*
import com.glu.stardomki.game.view.SplashActivity
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class WaiteActivity : AppCompatActivity() {

    private lateinit var bindWaite: ActivityWaiteBinding
    private var checker: String = "null"

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindWaite = ActivityWaiteBinding.inflate(layoutInflater)
        setContentView(bindWaite.root)

        deePP(this)

        val sp = getSharedPreferences("ActivityPREF", MODE_PRIVATE)
        if (sp.getBoolean("activity_exec", false)) {
            val sharPref = getSharedPreferences("SP", MODE_PRIVATE)
            when (sharPref.getString(CH, "null")) {
                "2" -> {
                    skipMe()
                }
                "3" -> {
                   testMeUAC()
                }
                "4" -> {
                    testWV()
                }
                "nm" -> {
                    testWV()
                }
                "dp" -> {
                    testWV()
                }
                "org" -> {
                    skipMe()
                }
                else -> {
                    skipMe()
                }
            }

        } else {
            //первое включение
            val exec = sp.edit()
            exec.putBoolean("activity_exec", true)
            exec.apply()

            val job = GlobalScope.launch(Dispatchers.IO) {
                checker = getCheckCode(URL_APPS_1+URL_APPS_2)
            }

            runBlocking {
                try {
                    job.join()
                } catch (_: Exception){
                }
            }

            when (checker) {
                "1" -> {
                    AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionDataListener, applicationContext)
                    AppsFlyerLib.getInstance().start(this)
                    afNullRecordedOrNotChecker(1500)
                }

                "2" -> {
                    skipMe()
                }

                "3" -> {
                    AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionDataListener, applicationContext)
                    AppsFlyerLib.getInstance().start(this)
                    afRecordedForUAC(1500)
                }

                "4" -> {
                    testWV()
                }

            }
        }
    }



    private suspend fun getCheckCode(link: String): String {
        val url = URL(link)
        val oneStr = "1"
        val twoStr = "2"
        val testStr = "3"
        val fourStr = "4"
        val activeStrn = "0"
        val urlConnection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection

        return try {
            when (val text = urlConnection.inputStream.bufferedReader().readText()) {
                "1" -> {
                    Log.d("jsoup status", text)
                    oneStr
                }
                "2" -> {
                    val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
                    val editor = sharPref.edit()
                    editor.putString(CH, twoStr)
                    editor.apply()
                    Log.d("jsoup status", text)
                    twoStr
                }
                "3" -> {
                    val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
                    val editor = sharPref.edit()
                    editor.putString(CH, testStr)
                    editor.apply()
                    Log.d("jsoup status", text)
                    testStr
                }
                "4" -> {
                    val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
                    val editor = sharPref.edit()
                    editor.putString(CH, fourStr)
                    editor.apply()
                    fourStr
                } else -> {
                    Log.d("jsoup status", "is null")
                    activeStrn
                }
            }
        } finally {
            urlConnection.disconnect()
        }

    }

    private fun afNullRecordedOrNotChecker(timeInterval: Long): Job {
        val sharPref = getSharedPreferences("SP", MODE_PRIVATE)

        return CoroutineScope(Dispatchers.IO).launch {
            while (NonCancellable.isActive) {

                val hawk1: String? = sharPref.getString(HW_C1, null)
                val hawkdeep: String? = sharPref.getString(HW_D1, "null")

                if (hawk1 != null) {

                    Log.d("TestInUIHawk", hawk1.toString())
                    if(hawk1.contains("tdb2")){
                        Log.d("zero_filter_2", "hawkname received")
                        val editor = sharPref.edit()
                        editor.putString(CH, "nm")
                        editor.apply()
                        testWV()
                    } else if (hawkdeep != null) {
                        if(hawkdeep.contains("tdb2")) {
                                Log.d("zero_filter_2", "hawkdeep received")
                                testWV()
                        } else {
                            Log.d("zero_filter_2", "hawkdeep wrong")
                            val editor = sharPref.edit()
                            editor.putString(CH, "org")
                            editor.apply()
                            skipMe()
                        }
                    }
                    break
                } else {
                    val hawk1: String? = sharPref.getString(HW_C1, null)
                    Log.d("TestInUIHawkNulled", hawk1.toString())
                    delay(timeInterval)
                }
            }
        }
    }

    private fun afRecordedForUAC(timeInterval: Long): Job {
        val sharPref = getSharedPreferences("SP", MODE_PRIVATE)
        return CoroutineScope(Dispatchers.IO).launch {
            while (NonCancellable.isActive) {
                val hawk1: String? = sharPref.getString(HW_C1, null)
                if (hawk1 != null) {
                    Log.d("dev_test", "Hawk!null")
                    testMeUAC()
                    break

                } else {
                    sharPref.getString(HW_C1, null)
                    delay(timeInterval)
                }
            }
        }
    }

    private val conversionDataListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
            val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
            val editor = sharPref.edit()
            val dataGotten = data?.get("campaign").toString()
            editor.putString(HW_C1,dataGotten)
            editor.apply()
        }

        override fun onConversionDataFail(p0: String?) {
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
        }

        override fun onAttributionFailure(p0: String?) {
        }
    }


    private fun skipMe() {
        Intent(this, SplashActivity::class.java)
            .also { startActivity(it) }
        finish()
    }

    private fun testMeUAC() {
        Intent(this, FilterActivity::class.java)
            .also { startActivity(it) }
        finish()
    }

    private fun testWV() {
        Intent(this, Web::class.java)
            .also { startActivity(it) }
        finish()
    }

    private fun deePP(context: Context) {
        val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
        val editor = sharPref.edit()
        AppLinkData.fetchDeferredAppLinkData(
            context
        ) { appLinkData: AppLinkData? ->
            appLinkData?.let {
                val params = appLinkData.targetUri.host
                //тест
                editor.putString(HW_D1,params.toString())
                editor.apply()
                    if (params!!.contains("tdb2")){
                        editor.putString(CH, "dp")
                        editor.apply()
                    }

            }
            if (appLinkData == null) {
//                //тест
//                editor.putString(D1,"tdb2vasyaidinahui")
//                editor.apply()
            }

        }
    }
}