package com.example.capstone_android

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.capstone_android.CreateViewFragment
import com.example.capstone_android.SelectFragment

import com.example.capstone_android.R
import com.example.capstone_android.databinding.ActivityCreateBinding

class CreateActivity: AppCompatActivity() {
    private lateinit var binding:ActivityCreateBinding
    var createViewFragment=CreateViewFragment()
    var selectViewFragment=SelectFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener{
            finish()
        }
        println(intent.getStringExtra("create"))
        supportFragmentManager.beginTransaction().replace(R.id.createfragment,createViewFragment).commit()




    }

    override fun onDestroy() {
        super.onDestroy()
        println("종료")
        val newintent= Intent()
        newintent.putExtra("result",1)
        setResult(RESULT_OK,newintent)
    }

    fun changeFragment(index:Int,hobby:String){
        when(index){
            1->{
                supportFragmentManager.beginTransaction().replace(R.id.createfragment,selectViewFragment).commit()
            }
            2->{
                var bundle=Bundle()
                bundle.putString("hobby",hobby)
                createViewFragment.arguments=bundle
                supportFragmentManager.beginTransaction().replace(R.id.createfragment,createViewFragment).commit()
            }
        }
    }
}
