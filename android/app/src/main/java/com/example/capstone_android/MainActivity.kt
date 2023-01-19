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

        //테스트용 실제로는 이  activity에 진입할 때는 이미 로그인이 되어 있을 것임
        Firebase.auth.signInWithEmailAndPassword("a@a.com","123456").addOnCompleteListener {
            //로그인 성공할 때 함수 정의
            Toast.makeText(
                this,
                "환영합니다.",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MeetingRoomActivity::class.java)
            intent.putExtra("meeting_room_id", "")
            startActivity(intent)

        }
    }
}