package com.example.capstone_android.Util

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log

class App:Application() {
    companion object{
        lateinit var instance:App
            private set
    }
    override fun onCreate(){
        super.onCreate()
        instance=this
        Log.d(TAG,instance.toString()+"어플리케이션 생성")
    }
}