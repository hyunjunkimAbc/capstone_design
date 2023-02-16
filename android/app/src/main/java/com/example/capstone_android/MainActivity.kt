
package com.example.capstone_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        //val intent = Intent(this, MeetingRoomActivity::class.java)
        //            intent.putExtra("meeting_room_id", "ce5vmU58GHfPTNhDtmfR")
        //            startActivity(intent)
    }
}
