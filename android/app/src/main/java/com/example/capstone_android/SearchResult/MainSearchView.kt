package com.example.capstone_android.SearchResult

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.HomeActivity
import com.example.capstone_android.MeetingList.MeetingViewModel
import com.example.capstone_android.SearchAddress.SearchHistoryRecyclerview
import com.example.capstone_android.SearchAddress.SharedPrefManager
import com.example.capstone_android.data.SearchData
import com.example.capstone_android.databinding.ActivitySearchopenviewBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainSearchView:AppCompatActivity(), SearchHistoryRecyclerview,android.widget.SearchView.OnQueryTextListener {
    private lateinit var SearchviewModel: MeetingViewModel
    private var popularsearch:ArrayList<TextView> = arrayListOf<TextView>()
    private var popularsearch2:ArrayList<TextView> = arrayListOf<TextView>()
    private var mainsearchHistoryList:ArrayList<SearchData> =ArrayList<SearchData>()
    private val mainhistoryItems = arrayListOf<SearchData>()
    private val MainHistoryAdapter = MainSearchAdapter(mainhistoryItems,this)
    private lateinit var binding: ActivitySearchopenviewBinding
    lateinit var db : FirebaseFirestore
    var MainSearchViewExpaned:android.widget.SearchView?=null
    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchopenviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= Firebase.firestore
        val toolbar=binding.searchviewtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        MainSearchViewExpaned=binding.searchviewsearch
        popularsearch.add(binding.firstsearchitem)
        popularsearch.add(binding.secondsearchitem)
        popularsearch.add(binding.thirdsearchitem)
        popularsearch.add(binding.fourthsearchitem)
        popularsearch.add(binding.fifthsearchitem)
        popularsearch.add(binding.sixthsearchitem)
        popularsearch.add(binding.seventhsearchitem)
        popularsearch.add(binding.eigthsearchitem)
        popularsearch.add(binding.ninthsearchitem)
        popularsearch.add(binding.tenthsearchitem)

        popularsearch2.add(binding.firstpopitem2)
        popularsearch2.add(binding.secondpopitem)
        popularsearch2.add(binding.thirdpopitem)
        popularsearch2.add(binding.fourpopitem)
        popularsearch2.add(binding.fifpopitem)
        popularsearch2.add(binding.sixthpopitem)
        popularsearch2.add(binding.sevenpopitem)
        popularsearch2.add(binding.eightpopitem)
        popularsearch2.add(binding.ninepopitem)
        popularsearch2.add(binding.tenpopitem)


        SearchviewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        SearchviewModel.MainSearchpopular.observe(this){populardata->
            for((index,data) in populardata.withIndex()){
                popularsearch[index].text=data.id
            }
            val lastUpdatedTime = Date()
            val lastUpdatedTimeString = SimpleDateFormat("HH:mm").format(lastUpdatedTime)
            binding.nowtime.text=lastUpdatedTimeString.plus(" 기준")
        }
        SearchviewModel.MainSearchpopular2.observe(this){popdata->
            for((index,data) in popdata.withIndex()){
                popularsearch2[index].text=data.id
            }
        }
        SearchviewModel.loadMainSearchData()
        SearchviewModel.loadMainPopData()
        MainSearchViewExpaned?.setOnQueryTextListener(this)

        this.mainsearchHistoryList= SharedPrefManager.maingetSearchHistoryList() as ArrayList<SearchData>
        this.mainsearchHistoryList.forEach{
            Log.d(TAG,"저장된 검색기록 = ${it.searchdata} , ${it.timestamp}")
            mainhistoryItems.add(it)
            mainhistoryItems.sortByDescending { it.timestamp }
        }
        binding.mainsearchrecyclerview.layoutManager= LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        binding.mainsearchrecyclerview.adapter=MainHistoryAdapter

        binding.searchviewremovebtn.setOnClickListener{
            SharedPrefManager.clearSearchHistoryList()
            this.mainsearchHistoryList.clear()
            this.mainhistoryItems.clear()
            SharedPrefManager.mainstoreSearchHistoryList(this.mainsearchHistoryList)
            this.MainHistoryAdapter.notifyDataSetChanged()

        }

        binding.searchresultswipe.setOnRefreshListener {
            SearchviewModel.loadMainSearchData()
            binding.searchresultswipe.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchItemDeleteClicked(position: Int) {
        this.mainsearchHistoryList.removeAt(position)
        this.mainhistoryItems.removeAt(position)
        SharedPrefManager.mainstoreSearchHistoryList(this.mainsearchHistoryList)
        this.MainHistoryAdapter.notifyDataSetChanged()
    }

    override fun onSearchItemClicked(position: Int) {
        val keyword=this.mainhistoryItems[position].searchdata
        var intent= Intent(this, MainSearchResult::class.java)
        intent.putExtra("searchdata",keyword)
        startActivity(intent)
       //
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            val currentTime = Date()
            val data=hashMapOf("Count" to FieldValue.increment(1),"timestamp" to currentTime)
            db.collection("Search").document(query).set(data, SetOptions.merge()).addOnSuccessListener{
                Log.d(TAG,"사용자 검색어 데이터 DB 저장 완료")
            }.addOnFailureListener{
                Log.d(TAG,"사용자 검색어 데이터 DB 저장 실패")
            }
            insertSearchTermHistory(query)
            var intent= Intent(this, MainSearchResult::class.java)
            intent.putExtra("searchdata",query)
            startActivity(intent)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val userInputText=newText?:""
        if(userInputText.count()==12){
            Toast.makeText(this,"검색어는 12자 까지만 입력 가능합니다", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun insertSearchTermHistory(searchTerm:String){
        val indexListToRemove = ArrayList<Int>()

        this.mainsearchHistoryList.forEachIndexed{index,searchdataItem->
            if(searchdataItem.searchdata==searchTerm){
                indexListToRemove.add(index)
            }
        }
        indexListToRemove.forEach{
            this.mainsearchHistoryList.removeAt(it)
            this.mainhistoryItems.removeAt(it)
        }
        val newSearchData=SearchData(searchdata = searchTerm, timestamp = SimpleDateFormat("HH:mm:ss").format(
            Date()
        ).toString())
        this.mainsearchHistoryList.add(newSearchData)
        this.mainhistoryItems.add(newSearchData)
        this.mainhistoryItems.sortByDescending { it.timestamp }
        this.MainHistoryAdapter.notifyDataSetChanged()
        SharedPrefManager.mainstoreSearchHistoryList(this.mainsearchHistoryList)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                setResult(RESULT_OK)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}