package com.example.capstone_android.SearchResult

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
import com.example.capstone_android.MainHomeActivity.LightingAdapter
import com.example.capstone_android.MeetingList.ListLightAdapter
import com.example.capstone_android.MeetingList.ListperiodicAdapter
import com.example.capstone_android.MeetingList.MeetingViewModel
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.lightData
import com.example.capstone_android.databinding.FragmentSearchlightBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchLightFragment(val searchdata:String): Fragment() {
    private lateinit var binding: FragmentSearchlightBinding
    private lateinit var LightlistAdapter : ListLightAdapter
    private lateinit var viewModel: MeetingViewModel
    private lateinit var recyclerview: RecyclerView
    lateinit var db : FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchlightBinding.inflate(inflater, container, false)
        db= Firebase.firestore
        viewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        recyclerview=binding.searchlightrecyclerview
        recyclerview.layoutManager = LinearLayoutManager(activity)
        LightlistAdapter= ListLightAdapter(emptyList())
        recyclerview.adapter = LightlistAdapter
        LightlistAdapter.setItemClickListener(object: ListLightAdapter.LightMeetingItemClickListener{
            override fun onClick(v: View, position: Int) {
                println(SingleTonData.clubdata[position].Uid)
            }
        })
        viewModel.SearchLightItemList.observe(viewLifecycleOwner) { data ->
            LightlistAdapter.sublightmmeetingitList(data)
        }


        SingleTonData.lightdata.clear()
        RoadData()
        lifecycleScope.launch {

            val roominfo = db.collection("lighting_meeting_room").whereEqualTo("category",searchdata).get().await()
            for(data2 in roominfo){
                SingleTonData.lightdata.add(data2.toObject(lightData::class.java))
                SingleTonData.lightdata.sortByDescending { it.address }
            }
            viewModel.SearchLightItemList.value= SingleTonData.lightdata
            ClearData()
        }
        return binding.root
    }
    private fun RoadData(){
        binding.pgbar2.visibility= View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){

        //binding.constraintContainPrograss.removeView(binding.mainhomeprogressbar)
        //this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        println("remove 성공")
        binding.pgbar2.visibility= View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}