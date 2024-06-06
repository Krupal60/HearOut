package com.hearout.app

import android.app.Application

class HearOut : Application() {

    override fun onCreate() {
        hearOut = this
        super.onCreate()
    }

    override fun onTerminate() {
        hearOut = null
        super.onTerminate()
    }

    companion object{
        var hearOut : HearOut? = null
            private set
    }
}