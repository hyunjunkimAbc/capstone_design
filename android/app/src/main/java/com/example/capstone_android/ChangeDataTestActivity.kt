package com.example.capstone_android

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivityChangeDataTestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


// ProfileFragment 장소 값 주고 받는 것 테스트용
// 위도 경도 값 필요 없음
// address만 잘 변경하면 됨
class ChangeDataTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeDataTestBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserUid: String
    private var storage = FirebaseStorage.getInstance()
    private var db = FirebaseFirestore.getInstance()

    private var editTimeForUpload: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeDataTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //프로필 프래그먼트에서 온 값
        var addressFromPF = intent.getStringExtra("addressFromPF")
        binding.editTextChangeAddress.setText(addressFromPF)

        val resultIntent = Intent()
        /*if(addressFromPF == ""){
            resultIntent.putExtra("addressFromActivity","기본 주소 설정")
        }else{
            resultIntent.putExtra("addressFromActivity","주소 자동 설정")
        }*/


        // 액티비티에서 프래그먼트로 값 전달
        binding.buttonChangeAddress.setOnClickListener(){
            var txt = binding.editTextChangeAddress.text.toString()
            resultIntent.putExtra("address", txt)

            setResult(Activity.RESULT_OK,resultIntent)
            finish()
        }
    }

}