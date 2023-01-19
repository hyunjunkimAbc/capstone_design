package com.example.capstone_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.ActivityMeetingRoomBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MeetingRoomActivity : AppCompatActivity() {
    //B4 C2 관련
    private val binding by lazy {
        ActivityMeetingRoomBinding.inflate(layoutInflater)
    }
    private val viewModel : MeetingRoomInfoViewModel by viewModels<MeetingRoomInfoViewModel>()

    private val meetingRoomInfoViewModel : MeetingRoomInfoViewModel by viewModels<MeetingRoomInfoViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        meetingRoomInfoViewModel.meetingRoomId = intent?.getStringExtra("meeting_room_id") ?: ""
        //만약 문제 생기면 super.onCreate(savedInstanceState)보다 먼저 작성해야 될수도 있음
        val meetingFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val meetingNavController = meetingFragment.navController
        val meetingNavigationView = binding.bottomNav
        meetingNavigationView.setupWithNavController(meetingNavController)


    }
}