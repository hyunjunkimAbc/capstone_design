package com.example.capstone_android.SearchResult

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.MeetingList.ListPlaceAdapter
import com.example.capstone_android.MeetingList.MeetingViewModel
import com.example.capstone_android.MeetingRoomActivity
import com.example.capstone_android.MeetingRoomDataManager
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.PlaceData
import com.example.capstone_android.databinding.FragmentSearchplaceBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchPlaceFragment (val searchdata:String): Fragment() {
    private lateinit var binding: FragmentSearchplaceBinding
    private lateinit var PlacelistAdapter: ListPlaceAdapter
    private lateinit var viewModel: MeetingViewModel
    private lateinit var recyclerview: RecyclerView
    lateinit var db : FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchplaceBinding.inflate(inflater, container, false)
        db= Firebase.firestore
        viewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        recyclerview=binding.searchplacerecyclerview
        recyclerview.layoutManager = LinearLayoutManager(activity)
        PlacelistAdapter= ListPlaceAdapter(emptyList())
        recyclerview.adapter = PlacelistAdapter
        PlacelistAdapter.setPlaceItemClickListener(object: ListPlaceAdapter.PlaceItemClickListener{
            override fun onClick(v: View, position: Int) {
                SingleTonData.placedata[position].Uid?.let {
                    gotoMeetingRoomActivity(
                        MeetingRoomDataManager.collectionNameOfPlaceRental,
                        it
                    )
                }
            }
        })
        viewModel.SearchPlaceItemList.observe(viewLifecycleOwner) { data ->
            PlacelistAdapter.subPlaceList(data)
        }


        SingleTonData.placedata.clear()
        RoadData()
        lifecycleScope.launch {

            val roominfo = db.collection("place_rental_room").whereEqualTo("category",searchdata).get().await()
            for(data2 in roominfo){
                SingleTonData.placedata.add(data2.toObject(PlaceData::class.java))
                SingleTonData.placedata.sortByDescending { it.address }
            }
            viewModel.SearchPlaceItemList.value= SingleTonData.placedata
            ClearData()
        }
        return binding.root
    }
    private fun RoadData(){
        binding.pgbar3.visibility= View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){

        //binding.constraintContainPrograss.removeView(binding.mainhomeprogressbar)
        //this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        println("remove 성공")
        binding.pgbar3.visibility= View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun gotoMeetingRoomActivity(colName:String,meetingRoomUid:String){
        println("5 21 collectionName ${colName} meeting_room_id ${meetingRoomUid}")
        var intent= Intent(context, MeetingRoomActivity::class.java)
        println("5-26-1 ${meetingRoomUid}")
        intent.putExtra("collectionName",colName)
        intent.putExtra("meeting_room_id", meetingRoomUid)
        startActivity(intent)
    }
}
