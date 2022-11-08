package com.glu.stardomki

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApppppCL: Application() {

    companion object {

        const val ONESIGNAL_APP_ID = "26dff950-837c-44ab-8f1d-259b2934b1e2"
        const val AF_DEV_KEY = "mjrRJgL6h7pupTMc3te2zm"
        const val jsoupCheck = "1v5b"

        const val linkAppsCheckPart1 = "http://power"
        const val linkAppsCheckPart2 = "ofarch.xyz/apps.txt"

        const val linkFilterPart1 = "http://power"
        const val linkFilterPart2 = "ofarch.xyz/go.php?to=1&"

        const val odone = "sub_id_1="

        var MAIN_ID: String? = ""
        var C1: String? = "c11"
        var C13: String? = "c13"
        var D1: String? = "d11"
        var CH: String? = "check"
    }

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
}

class Adv (context: Context) {

    private val adInfo = AdvertisingIdClient(context.applicationContext)
    suspend fun getAdvertisingId(): String =
        withContext(Dispatchers.IO) {
            adInfo.start()
            val adIdInfo = adInfo.info
            Log.d("getAdvertisingId = ", adIdInfo.id.toString())
            adIdInfo.id

    }
}