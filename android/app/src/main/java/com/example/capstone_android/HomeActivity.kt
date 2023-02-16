package com.example.capstone_android

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class HomeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    val st = FirebaseStorage.getInstance()

    var profile_image_existence = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var user = FirebaseAuth.getInstance().currentUser

        // DB에 있는 닉네임 띄우기
        val user_data = db.collection("user")
        user_data.get().addOnSuccessListener {
            for(d in it){
                if((user?.uid ?: String)==d["uid"]){
                    binding.textView3.text = "${d["nickname"]}"
                }
            }
        }


        // 스토리지에 있는 profile image 이미지뷰에 띄우기
        val ref = rootRef.child("user_profile_image/"+"${(user?.uid ?: String)}") // 이미지 파일 이름 가져옴
        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
            if (it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                binding.imageView.setImageBitmap(bmp)
            }
            // 프로필 이미지가 없을때 디폴트 이미지 리소스 띄우기
            else{
                val default_img = resources.getDrawable(R.drawable.profile_default_img)
                binding.imageView.setImageDrawable(default_img)
            }
        }


    }
}