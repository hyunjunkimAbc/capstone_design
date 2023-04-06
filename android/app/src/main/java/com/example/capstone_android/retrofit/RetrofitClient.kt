package com.example.capstone_android.retrofit

import android.content.ContentValues.TAG
import android.util.Log
import com.example.capstone_android.SearchTextFragment
import com.example.capstone_android.Util.isJsonArray
import com.example.capstone_android.Util.isJsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.log

object  RetrofitClient {
    private var retrofitClient: Retrofit?=null

    fun getClient(baseUrl:String):Retrofit?{
        //로깅 인터셉터

        //okhttp 인스턴스 생성
        val client=OkHttpClient.Builder()

        //로그를 찍기위해 로깅 인터셉터
        val loggingIntercepter=HttpLoggingInterceptor(object:HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Log.d(TAG,"RetrofitClient-log() called /message :$message")
                when{
                    message.isJsonObject()-> Log.d(TAG,JSONObject(message).toString(4))
                    message.isJsonArray()-> Log.d(TAG,JSONObject(message).toString(4))
                    else->{
                        try{
                            Log.d(TAG,JSONObject(message).toString(4))
                        }catch (e:Exception){
                            Log.d(TAG,message)
                        }
                    }
                }
            }
        })
        loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        //위에서 성공한 로깅 인터센텁를 okhttp에 추가
        client.addInterceptor(loggingIntercepter)

        //커넥션 타임아웃
        client.connectTimeout(10,TimeUnit.SECONDS)
        client.readTimeout(10,TimeUnit.SECONDS)
        client.writeTimeout(10,TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true)

        if(retrofitClient==null){
            retrofitClient=Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }
        return retrofitClient
    }
}