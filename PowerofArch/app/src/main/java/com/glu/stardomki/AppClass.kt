package com.glu.stardomki

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.*

class AppClass: Application() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch(Dispatchers.IO) {
            applyDeviceId(context = applicationContext)
        }

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

    private suspend fun applyDeviceId(context: Context) {
        val advertisingInfo = Adv(context)
        val idInfo = advertisingInfo.getAdvertisingId()

        val prefs = getSharedPreferences("SP", MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString(MAIN_ID, idInfo)
        editor.apply()
    }

    companion object {

        const val ONESIGNAL_APP_ID = "26dff950-837c-44ab-8f1d-259b2934b1e2"
        const val AF_DEV_KEY = "mjrRJgL6h7pupTMc3te2zm"
        const val jsoupCheck = "1v5b"

        const val URL_APPS_1 = "http://power"
        const val URL_APPS_2 = "ofarch.xyz/apps.txt"

        const val FILTER_URL_1 = "http://power"
        const val FILTER_URL_2 = "ofarch.xyz/go.php?to=1&"

        const val DONE = "sub_id_1="

        var MAIN_ID: String? = ""
        var HW_C1: String? = "c11"
        var HW_D1: String? = "d11"
        var CH: String? = "check"
    }
}

class Adv (context: Context) {
    private val adInfo = AdvertisingIdClient(context.applicationContext)
    suspend fun getAdvertisingId(): String = withContext(Dispatchers.IO) {
            adInfo.start()
            val adIdInfo = adInfo.info
            Log.d("getAdvertisingId = ", adIdInfo.id.toString())
            adIdInfo.id
    }
}

