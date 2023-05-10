package com.example.capstone_android.retrofit

import com.example.capstone_android.BuildConfig

enum class RESPONSESTATE{
        OKAY,
        FAIL
}

object ApiInfo{
        const val BASE_URL = "https://apis.openapi.sk.com/"
        const val API_KEY =  BuildConfig.TMAP_API_KEY// REST API 키
        const val SearchPOI="tmap/pois?version=1&format=json&callback=result&count=20"
}