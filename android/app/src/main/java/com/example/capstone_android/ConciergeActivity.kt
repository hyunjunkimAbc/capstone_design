package com.example.capstone_android

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.ActivityConciergeBinding
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
    private val viewModelLightingMeetingRoom : LightingViewModel by viewModels<LightingViewModel>()
    private val viewModelPeriodicMeetingRoom : PeriodicViewModel by viewModels<PeriodicViewModel>()
    private val viewModelPlaceRentalRoom : PlaceRentalViewModel by viewModels<PlaceRentalViewModel>()
    private val viewModelCompetitionRoom : CompetitionViewModel by viewModels<CompetitionViewModel>()
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

        setRecyclerView(LightingAdapter(viewModelLightingMeetingRoom),viewModelLightingMeetingRoom,binding.lightingMeetingRecyclerView)
        setRecyclerView(PeriodicAdapter(viewModelPeriodicMeetingRoom),viewModelPeriodicMeetingRoom,binding.periodicMeetingRecyclerView)
        setRecyclerView(PlaceRentalAdapter(viewModelPlaceRentalRoom),viewModelPlaceRentalRoom,binding.placeRentalRecyclerView)
        setRecyclerView(CompetitionAdapter(viewModelCompetitionRoom),viewModelCompetitionRoom,binding.competitionRecyclerView)


        addToRecyclerView(MeetingRoomDataManager.collectionNameOfLightingMeetingRoom,
            viewModelLightingMeetingRoom)

        addToRecyclerView(MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom,
            viewModelPeriodicMeetingRoom)
        addToRecyclerView(MeetingRoomDataManager.collectionNameOfPlaceRental,
            viewModelPlaceRentalRoom)
        addToRecyclerView(MeetingRoomDataManager.collectionNameOfCompetition,
            viewModelCompetitionRoom)
        setContentView(binding.root)
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
                            meetingRoom.data["title"] as String,meetingRoom.id,collectionName))
                    }else{
                        var ref = rootRef.child("${collectionName}/default.jpg")
                        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                            if(it.isSuccessful){
                                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                                viewModel.addItem(MeetingRoom(bmp,meetingRoom.data["title"] as String,meetingRoom.id,collectionName))
                            }else{
                                println("undefined err")
                            }
                        }
                    }
                }

            }
        }
    }
    //test
    fun setRecyclerView(adapter: ConciergeAdapter,viewModel: ConciergeViewModel,view: androidx.recyclerview.widget.RecyclerView){
        //val meetingMembersRecyclerView = v.findViewById<RecyclerView>(R.id.meetingMembersRecyclerView)
        val MeetingRecyclerView = view
        MeetingRecyclerView.adapter = adapter
        MeetingRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        MeetingRecyclerView.setHasFixedSize(true)

        viewModel.itemsListData.observe(this){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(this){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
            println("---------------${viewModel.items[i!!].title}")
            val intent = Intent(this, MeetingRoomActivity::class.java)
            //테스트라서 이렇게 했지만 나중에는 동경님 activity로 옮겨야 함
            intent.putExtra("collectionName", viewModel.items[i!!].col_name)
            intent.putExtra("meeting_room_id",viewModel.items[i!!].document_id)
            startActivity(intent)
            //val bundle = bundleOf("document_id" to viewModelLightingMeetingRoom.items[i!!].document_id)
            //findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_showPostingFragment,bundle)
        }
        registerForContextMenu(MeetingRecyclerView)
    }
}