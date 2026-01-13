package com.aitextcleaner

import android.app.Application
import com.aitextcleaner.ads.AdMobManager

class AITextCleanerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AdMobManager.initialize(this)
    }
}
