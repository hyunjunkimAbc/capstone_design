package com.example.capstone_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone_android.databinding.ActivitySearchmapBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchMap:AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivitySearchmapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchmapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= Firebase.firestore
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.textview.text="주소설정"
        val searchtext=SearchTextFragment()

        supportFragmentManager.beginTransaction().replace(R.id.search_content,searchtext).commit()
    }



}