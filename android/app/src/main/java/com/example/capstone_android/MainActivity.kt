package com.example.capstone_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Firebase.auth.signInWithEmailAndPassword("a@a.com","123456").addOnCompleteListener {
            //로그인 성공할 때 함수 정의
            Toast.makeText(
                this,
                "환영합니다.",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MeetingRoomActivity::class.java)
            intent.putExtra("meeting_room_id", "ce5vmU58GHfPTNhDtmfR")
            startActivity(intent)

        }
    }
}