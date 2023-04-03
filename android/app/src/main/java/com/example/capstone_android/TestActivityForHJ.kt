package com.example.capstone_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestActivityForHJ : AppCompatActivity() {
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
        setContentView(R.layout.activity_test_for_hj)
        val colName  = intent.getStringExtra("collectionName")
        val intent = Intent(this, MeetingRoomActivity::class.java)
        println("---------collectionName ${colName}")
        //테스트라서 이렇게 했지만 나중에는 동경님 activity로 옮겨야 함
        intent.putExtra("collectionName",colName)
        if(colName ==collectionNameOfLightingMeetingRoom){
            intent.putExtra("meeting_room_id","t0WNSZBkFeRiPWgYAqpe")
        }else if(colName == collectionNameOfPeriodicMeetingRoom){
            intent.putExtra("meeting_room_id","penBe1ozoQhLyAJuuTkq")
        }else if(colName ==collectionNameOfPlaceRental){
            intent.putExtra("meeting_room_id","4wi6xuwF8HWc8ODUpD9G")
        }else if(colName == collectionNameOfCompetition){
            intent.putExtra("meeting_room_id","Edmc1NmZljVChY1PUJXZ")
        }

        startActivity(intent)
    }
}