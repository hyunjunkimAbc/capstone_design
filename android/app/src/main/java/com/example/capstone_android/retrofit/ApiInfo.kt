package com.example.capstone_android.retrofit

enum class RESPONSESTATE{
        OKAY,
        FAIL
}

object ApiInfo{
        const val BASE_URL = "https://apis.openapi.sk.com/"
        const val API_KEY =  "VwYv1tFJtY1v9qhvVmkP92XdfO8UF8Kj3Hu83jRL" // REST API 키
        const val SearchPOI="tmap/pois?version=1&format=json&callback=result&count=20"
}