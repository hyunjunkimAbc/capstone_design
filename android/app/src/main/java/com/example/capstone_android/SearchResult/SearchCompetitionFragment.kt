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
import com.example.capstone_android.MeetingList.ListCompetitionAdapter
import com.example.capstone_android.MeetingList.ListPlaceAdapter
import com.example.capstone_android.MeetingList.MeetingViewModel
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.CompetitionData
import com.example.capstone_android.data.PlaceData
import com.example.capstone_android.databinding.FragmentSearchcompetitionBinding
import com.example.capstone_android.databinding.FragmentSearchplaceBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchCompetitionFragment(val searchdata:String): Fragment() {
    private lateinit var binding: FragmentSearchcompetitionBinding
    private lateinit var CompetitionAdapter: ListCompetitionAdapter
    private lateinit var viewModel: MeetingViewModel
    private lateinit var recyclerview: RecyclerView
    lateinit var db : FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchcompetitionBinding.inflate(inflater, container, false)
        db= Firebase.firestore
        viewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        recyclerview=binding.searchcompetitionrecyclerview
        recyclerview.layoutManager = LinearLayoutManager(activity)
        CompetitionAdapter= ListCompetitionAdapter(emptyList())
        recyclerview.adapter = CompetitionAdapter
        CompetitionAdapter.setCompetitionItemClickListener(object: ListCompetitionAdapter.CompetitionItemClickListener{
            override fun onClick(v: View, position: Int) {
                println(SingleTonData.clubdata[position].Uid)
            }
        })
        viewModel.SearchCompetitionItemList.observe(viewLifecycleOwner) { data ->
            CompetitionAdapter.subCompetitionList(data)
        }


        SingleTonData.competitiondata.clear()
        RoadData()
        lifecycleScope.launch {

            val roominfo = db.collection("competition_room").whereEqualTo("category",searchdata).get().await()
            for(data2 in roominfo){
                SingleTonData.competitiondata.add(data2.toObject(CompetitionData::class.java))
                SingleTonData.competitiondata.sortByDescending { it.address }
            }
            viewModel.SearchCompetitionItemList.value= SingleTonData.competitiondata
            ClearData()
        }
        return binding.root
    }
    private fun RoadData(){
        binding.pgbar4.visibility= View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){

        //binding.constraintContainPrograss.removeView(binding.mainhomeprogressbar)
        //this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        println("remove 성공")
        binding.pgbar4.visibility= View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
