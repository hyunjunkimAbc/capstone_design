package com.example.capstone_android

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.data.LightingMeetingRoomData
import com.example.capstone_android.databinding.ActivityConciergeBinding
import com.example.capstone_android.databinding.ActivityMeetingRoomBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ConciergeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityConciergeBinding.inflate(layoutInflater)
    }
    val db = Firebase.firestore
    var rootRef = Firebase.storage.reference
    private val viewModelLightingMeetingRoom : ConciergeViewModel by viewModels<ConciergeViewModel>()
    private val viewModelPeriodicMeetingRoom : ConciergeViewModel by viewModels<ConciergeViewModel>()
    private val viewModelPlaceRentalRoom : ConciergeViewModel by viewModels<ConciergeViewModel>()
    private val viewModelCompetitionRoom : ConciergeViewModel by viewModels<ConciergeViewModel>()
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

        setRecyclerView(viewModelLightingMeetingRoom,binding.lightingMeetingRecyclerView)
        setRecyclerView(viewModelPeriodicMeetingRoom,binding.periodicMeetingRecyclerView)
        setRecyclerView(viewModelPlaceRentalRoom,binding.placeRentalRecyclerView)
        setRecyclerView(viewModelCompetitionRoom,binding.competitionRecyclerView)

        addToRecyclerView(MeetingRoomDataManager.collectionNameOfLightingMeetingRoom,
            viewModelLightingMeetingRoom)
        addToRecyclerView(MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom,
            viewModelPeriodicMeetingRoom)
        addToRecyclerView(MeetingRoomDataManager.collectionNameOfPlaceRental,
            viewModelPlaceRentalRoom)
        addToRecyclerView(MeetingRoomDataManager.collectionNameOfCompetition,
            viewModelCompetitionRoom)
    }
    fun inputDataAndStartActivity(collectionName:String){
        val intent = Intent(this, TestActivityForHJ::class.java)
        //테스트라서 이렇게 했지만 나중에는 동경님 activity로 옮겨야 함
        intent.putExtra("collectionName", collectionName)
        startActivity(intent)
    }
    fun addToRecyclerView(collectionName: String,viewModel: ConciergeViewModel){
        var meetingRoomCollection=db.collection(collectionName)
        println("test-----------------------")
        println(collectionName)
        meetingRoomCollection.get().addOnSuccessListener {
            for (meetingRoom in it){
                println(meetingRoom)
                var meetingInfoImage = rootRef.child("${collectionName}/${meetingRoom.id}.jpg")
                meetingInfoImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        viewModel.addItem(MeetingRoom(bmp,
                            meetingRoom.data["title"] as String,meetingRoom.id))
                    }else{
                        var ref = rootRef.child("${collectionName}/default.jpg")
                        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                            if(it.isSuccessful){
                                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                                viewModel.addItem(MeetingRoom(bmp,meetingRoom.data["title"] as String,meetingRoom.id))
                            }else{
                                println("undefined err")
                            }
                        }
                    }
                }

            }
        }
    }
    fun setRecyclerView(viewModel: ConciergeViewModel,view: RecyclerView){
        val adapter = ConciergeAdapter(viewModel)
        //val meetingMembersRecyclerView = v.findViewById<RecyclerView>(R.id.meetingMembersRecyclerView)
        val MeetingRecyclerView = view
        MeetingRecyclerView.adapter = adapter
        MeetingRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        MeetingRecyclerView.setHasFixedSize(true)
        viewModel.itemsListData.observe(this ){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(this){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
            //val bundle = bundleOf("document_id" to viewModelLightingMeetingRoom.items[i!!].document_id)
            //findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_showPostingFragment,bundle)
        }

        registerForContextMenu(MeetingRecyclerView)
    }
}