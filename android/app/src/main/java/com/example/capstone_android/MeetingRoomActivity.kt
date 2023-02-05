package com.example.capstone_android

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.capstone_android.databinding.ActivityMeetingRoomBinding



class MeetingRoomActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMeetingRoomBinding.inflate(layoutInflater)
    }
    private val meetingRoomInfoViewModel : MeetingRoomInfoViewModel by viewModels<MeetingRoomInfoViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //만약 문제 생기면 super.onCreate(savedInstanceState)보다 먼저 작성해야 될수도 있음
        val meetingFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val meetingNavController = meetingFragment.navController
        val meetingNavigationView =binding.bottomNav
        meetingNavigationView.setupWithNavController(meetingNavController)
    }
    override fun onBackPressed() {
        val meetingFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val meetingNavController = meetingFragment.navController
        if(meetingNavController.currentDestination?.id == R.id.showPostingFragment){
            findNavController(R.id.fragment).navigate(R.id.meetingRoomPostingsFragment)
        }else if(meetingNavController.currentDestination?.id == R.id.meetingRoomPostingAddFragment){
            findNavController(R.id.fragment).navigate(R.id.meetingRoomPostingsFragment)
        }
        else{
            this.finish()
        }
    }
}