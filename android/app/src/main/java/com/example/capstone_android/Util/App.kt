package com.example.capstone_android.Util

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {
    companion object{
        lateinit var instance:App
            private set
    }

    override fun onCreate(){
        super.onCreate()
        instance=this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Log.d(TAG,instance.toString()+"어플리케이션 생성")
    }
    /*
    fun getInstance(): Application {
        if (instance == null){
            instance = Application()
            return instance
        }else{
            return instance
        }
    }*/
}