package com.example.capstone_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone_android.data.LightingMeetingRoomData
import com.example.capstone_android.databinding.ActivityConciergeBinding
import com.example.capstone_android.databinding.ActivityMeetingRoomBinding

class ConciergeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityConciergeBinding.inflate(layoutInflater)
    }
    private val collectionNameOfLightingMeetingRoom =
        "lighting_meeting_room"
    private val collectionNameOfPeriodicMeetingRoom =
        "periodic_meeting_room"
    private val collectionNameOfPlaceRental =
        "place_rental_room"
    private val collectionNameOfCompetition =
        "competition_room"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lightingMeetingButton.setOnClickListener {
            inputDataAndStartActivity(collectionNameOfLightingMeetingRoom)
        }
        binding.periodicMeetingButton.setOnClickListener {
            inputDataAndStartActivity(collectionNameOfPeriodicMeetingRoom)
        }
        binding.placeRentalButton.setOnClickListener {
            inputDataAndStartActivity(collectionNameOfPlaceRental)
        }
        binding.competitionButton.setOnClickListener {
            inputDataAndStartActivity(collectionNameOfCompetition)
        }
    }
    fun inputDataAndStartActivity(collectionName:String){
        val intent = Intent(this, TestActivityForHJ::class.java)
        //테스트라서 이렇게 했지만 나중에는 동경님 activity로 옮겨야 함
        intent.putExtra("collectionName", collectionName)
        startActivity(intent)

    }
}