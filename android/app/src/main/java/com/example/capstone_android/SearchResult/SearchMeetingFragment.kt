package com.example.capstone_android.SearchResult

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.MeetingList.ListperiodicAdapter
import com.example.capstone_android.MeetingList.MeetingViewModel
import com.example.capstone_android.MeetingRoomDataManager
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.databinding.FragmentSearchmeetingBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchMeetingFragment(val searchdata:String): Fragment() {
    private lateinit var binding: FragmentSearchmeetingBinding
    private lateinit var PeriodiclistAdapter : ListperiodicAdapter
    private lateinit var viewModel: MeetingViewModel
    private lateinit var recyclerview:RecyclerView
    lateinit var db : FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchmeetingBinding.inflate(inflater, container, false)
        db= Firebase.firestore
        viewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        recyclerview=binding.searchmeetingrecyclerview
        recyclerview.layoutManager = LinearLayoutManager(activity)
        PeriodiclistAdapter=ListperiodicAdapter(emptyList())
        recyclerview.adapter = PeriodiclistAdapter
        PeriodiclistAdapter.setItemClickListener(object:ListperiodicAdapter.MeetingItemClickListener{
            override fun onClick(v: View, position: Int) {
                println(SingleTonData.clubdata[position].Uid)
            }
        })
        viewModel.SearchMeetingItemList.observe(viewLifecycleOwner) { data ->
            PeriodiclistAdapter.submmeetingitList(data)
        }


        SingleTonData.clubdata.clear()
        RoadData()
        lifecycleScope.launch {

            val roominfo = db.collection("meeting_room").whereEqualTo("category",searchdata).get().await()
            for(data2 in roominfo){
                SingleTonData.clubdata.add(data2.toObject(ClubData::class.java))
                SingleTonData.clubdata.sortByDescending { it.positionx }
            }
            viewModel.SearchMeetingItemList.value=SingleTonData.clubdata
            ClearData()
        }
        return binding.root
    }
    private fun RoadData(){
        binding.pgbar.visibility= View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){

        //binding.constraintContainPrograss.removeView(binding.mainhomeprogressbar)
        //this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        println("remove 성공")
        binding.pgbar.visibility=View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}