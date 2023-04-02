package com.example.capstone_android

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.databinding.ActivityHomeBinding
import com.example.capstone_android.databinding.ActivitySelecthobbyBinding
import com.example.capstone_android.hobbyfragment.SportFragment
import com.example.capstone_android.hobbyfragment.TripFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SelectHobbyActivity:AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivitySelecthobbyBinding
     var myhobbylist: ArrayList<String> = ArrayList<String>()
    val allhobbylist = arrayListOf<String>(
        "농구", "축구", "탁구", "테니스", "배드민턴",
        "야구", "볼링", "자전거", "골프", "런닝",
        "수영", "배구", "요가|필라테스", "태권도|유도", "복싱",
        "무술", "승마", "헬스", "롤러|보드", "스키|보드", "당구","등산","수상|레저"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelecthobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        val key=intent.getStringExtra("key")
        println(key)
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.textview.text="관심사 재설정"

        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { document ->
                val item = document.toObject(SignUpData::class.java)
                for (data in item?.interest_array!!) {
                    myhobbylist.add(data)
                }
            }


        binding.sportsImageButton.setOnClickListener{
            val sportfragment=SportFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            sportfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,sportfragment).commit()
        }
        binding.tripImageButton.setOnClickListener{
            val tripfragment=TripFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            tripfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,tripfragment).commit()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.changehobby_toolbar,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.toolbar_next_button->{
                println(myhobbylist)
                val data = hashMapOf("interest_array" to myhobbylist)
                db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).set(data, SetOptions.merge())
                finish()
                return true
            }
            R.id.cancel->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        println(myhobbylist)
        super.onDestroy()
    }
}