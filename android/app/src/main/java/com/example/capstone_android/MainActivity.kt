
package com.example.capstone_android

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        getDebugHashKey()
        //val intent = Intent(this, MeetingRoomActivity::class.java)
        //            intent.putExtra("meeting_room_id", "ce5vmU58GHfPTNhDtmfR")
        //            startActivity(intent)
    }
    private fun getDebugHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            if (packageInfo == null) Log.e("KeyHash", "KeyHash:null") else {
                for (signature in packageInfo.signatures) {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}
