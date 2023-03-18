package com.example.capstone_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivityCreateaddressBinding

class CreateAddress:AppCompatActivity() {
    var activity: Activity =this
    private lateinit var binding: ActivityCreateaddressBinding

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateaddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webview = binding.webview
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(BridgeInterface(), "Android")
        webview.webViewClient=WebViewClient()





    }

    private fun BridgeInterface() {
        @JavascriptInterface
        fun processDATA(data: String?) {
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}
