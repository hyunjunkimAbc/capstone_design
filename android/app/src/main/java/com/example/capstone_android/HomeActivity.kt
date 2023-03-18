package com.example.capstone_android
import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.data.lightningFragment
import com.example.capstone_android.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity: AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= Firebase.firestore
        val toolbar=binding.toolbar
       setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.textview.setOnClickListener{
            val intent=Intent(this,AddressActivity::class.java)
            startActivityForResult(intent,1)
        }
        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{ document->
            val item=document.toObject(SignUpData::class.java)
            val test=item?.address?.split(" ")
            binding.textview.text= test?.get(2)
        }
        val detailViewFragment=DetailViewFragment()
        supportFragmentManager.beginTransaction().replace(R.id.search_content,detailViewFragment).commit()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
        binding.bottomNavigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.action_home ->{
                    val detailViewFragment=DetailViewFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.search_content,detailViewFragment).commit()

                    return@setOnItemSelectedListener true
                }
                R.id.action_gps ->{
                    val mapViewFragment=MapFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.search_content,mapViewFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_Lightning->{
                    val lightningFragment=lightningFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.search_content,lightningFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_account ->{
                    val intent = Intent(this,ProfileActivity::class.java)
                    startActivity(intent)

                    return@setOnItemSelectedListener true
                }
                R.id.action_more ->{

                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{ document->
            val item=document.toObject(SignUpData::class.java)
            val test=item?.address?.split(" ")
            binding.textview.text= test?.get(2)
        }
    }
}