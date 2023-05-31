package com.example.capstone_android.SetProfile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.MeetingList.ListperiodicAdapter
import com.example.capstone_android.MeetingList.MeetingViewModel
import com.example.capstone_android.MeetingRoomActivity
import com.example.capstone_android.MeetingRoomDataManager
import com.example.capstone_android.Util.SingleTonData


import com.example.capstone_android.databinding.ActivitymymeetingBinding
import kotlinx.android.synthetic.main.fragment_main.view.*

class MyMeetingRoomActivity:AppCompatActivity() {
    private lateinit var binding: ActivitymymeetingBinding
    private lateinit var viewModel: MeetingViewModel
    private lateinit var PeriodiclistAdapter : ListperiodicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitymymeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar=binding.mymeetingtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "내 모임"
        val recyclerView: RecyclerView =binding.mymeetingroomrecycler
        viewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        recyclerView.layoutManager = LinearLayoutManager(this)
        PeriodiclistAdapter=ListperiodicAdapter(emptyList())
        recyclerView.adapter = PeriodiclistAdapter
        PeriodiclistAdapter.setItemClickListener(object:ListperiodicAdapter.MeetingItemClickListener{
            override fun onClick(v: View, position: Int) {
                SingleTonData.mymeetingroomdata[position].Uid?.let {
                    gotoMeetingRoomActivity(MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom,it)
                }
                println(SingleTonData.mymeetingroomdata[position].Uid)
            }
        })
        viewModel.MyMeetingRoomItemList.observe(this) { data ->
            PeriodiclistAdapter.submmeetingitList(data)
        }
        viewModel.loadMyMeetingData()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun gotoMeetingRoomActivity(colName:String,meetingRoomUid:String){
        println("5 21 collectionName ${colName} meeting_room_id ${meetingRoomUid}")
        var intent= Intent(this, MeetingRoomActivity::class.java)
        println("5-26-1 ${meetingRoomUid}")
        intent.putExtra("collectionName",colName)
        intent.putExtra("meeting_room_id", meetingRoomUid)
        startActivity(intent)
    }
}