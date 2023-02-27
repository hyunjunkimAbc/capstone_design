package com.example.capstone_android

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivityResetpasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResetPWActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityResetpasswordBinding.inflate(layoutInflater)
    }

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 상단 툴바
        val toolbar = binding.signupToolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        supportActionBar!!.setTitle("비밀번호 재설정") // 툴바 제목 설정


        binding.changeBtn.setOnClickListener(){
            println("재설정 버튼 클릭")
            if(binding.EmailEditText.getText().toString()==""){
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(binding.nicknameEditText.getText().toString()==""){
                Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                val user_data = db.collection("user")
                user_data.get().addOnSuccessListener {
                    for(d in it){
                        if(binding.EmailEditText.getText().toString()==d["email"]) {
                            if(binding.nicknameEditText.getText().toString()==d["nickname"]){
                                // 생년월일 선택 DatePicker
                                val datePicker = binding.changedpSpinner
                                var birthday = datePicker.year.toString()+datePicker.month.toString()+datePicker.dayOfMonth.toString()
                                if (birthday==d["birthday"]){
                                    findPassword()
                                    return@addOnSuccessListener
                                }
                                else {
                                    Toast.makeText(this, "생년월일이 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                    return@addOnSuccessListener
                                }
                            }
                            else Toast.makeText(this, "해당 이메일의 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Toast.makeText(this, "해당 이메일로 가입된 계정이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 액션버튼 클릭 했을 때
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

    fun findPassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.EmailEditText.getText().toString())
            .addOnCompleteListener {	task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "재설정 메일을 보냈습니다.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "메일을 다시 입력해주세요.", Toast.LENGTH_LONG).show()
                }
            }
    }
}