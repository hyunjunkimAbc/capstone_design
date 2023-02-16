package com.example.capstone_android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivitySignUpBinding


class SelectInterestActivity : AppCompatActivity(){
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //setContentView(R.layout.activity_signup)

        // 상단 툴바
//        val toolbar = binding.signupToolbar
        setSupportActionBar(binding.signupToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        supportActionBar!!.setTitle("관심사 선택") // 툴바 제목 설정


    }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                // toolbar의 back키 눌렀을 때 동작
                // 액티비티 뒤로 이동
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}