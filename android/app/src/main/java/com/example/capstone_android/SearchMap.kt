package com.example.capstone_android

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.Util.SharedPrefManager
import com.example.capstone_android.`interface`.SearchHistoryRecyclerview
import com.example.capstone_android.data.ListLayout
import com.example.capstone_android.data.ResultSearchKeyword
import com.example.capstone_android.data.SearchData
import com.example.capstone_android.databinding.ActivitySearchmapBinding
import com.example.capstone_android.retrofit.RESPONSESTATE
import com.example.capstone_android.retrofit.RetrofitManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_searchmap.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchMap:AppCompatActivity() ,SearchView.OnQueryTextListener,CompoundButton.OnCheckedChangeListener,
    View.OnClickListener, SearchHistoryRecyclerview {
    private var searchHistoryList=ArrayList<SearchData>()
    private val listItems = arrayListOf<ListLayout>() // 리사이클러 뷰 아이템
    private val historyItems = arrayListOf<SearchData>() // 리사이클러 뷰 아이템
    private val listAdapter = SearchTextAdapter(listItems) // 리사이클러 뷰 어댑터
    private val HistoryAdapter = SearchTextHistoryAdapter(historyItems,this) // 리사
    lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivitySearchmapBinding
    private lateinit var mySearchView:SearchView
    private lateinit var mySearchViewEditText:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchmapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= Firebase.firestore
        val toolbar=binding.topappbar
        toolbar.title="지역 및 장소검색"
        setSupportActionBar(toolbar)
        val  searchrecyclerView=binding.searchRecycler
        val historyrecyclerView=binding.searchHistoryRecycler

        searchrecyclerView.layoutManager= LinearLayoutManager(this)
        searchrecyclerView.adapter = listAdapter
        listAdapter.setItemClickListener(object: SearchTextAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                println("클긱")
            }
        })
        val myLinearLayoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
        myLinearLayoutManager.stackFromEnd=true
        historyrecyclerView.apply {
            this.layoutManager= myLinearLayoutManager
            this.scrollToPosition(HistoryAdapter.itemCount-1)
            this.adapter = HistoryAdapter

        }


        binding.searchHistoryModeSwitch.setOnCheckedChangeListener(this)
        binding.clearSearchHistoryButton.setOnClickListener(this)

        //저장된 검색기록 가져오기
        this.searchHistoryList=SharedPrefManager.getSearchHistoryList() as ArrayList<SearchData>
        this.searchHistoryList.forEach{
            Log.d(TAG,"저장된 검색기록 = ${it.searchdata} , ${it.timestamp}")
            historyItems.add(it)
        }
        handleSearchViewUi()
        binding.searchHistoryModeSwitch.isChecked=SharedPrefManager.checkSearchHistoryMode()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.top_app_bar_menu,menu)

        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        this.mySearchView=menu?.findItem(R.id.search_menu_item)?.actionView as SearchView
        this.mySearchView.apply{
            this.queryHint="지역 및 장소를 입력해주세요"
            this.setOnQueryTextListener(this@SearchMap)
            this.setOnQueryTextFocusChangeListener{_,hasExpaned->
                when(hasExpaned){
                    true->{
                        Log.d(TAG,"서치뷰 열림")
                        binding.linearSearchHistoryView.visibility=View.VISIBLE
                        binding.searchRecycler.visibility=View.INVISIBLE
                        handleSearchViewUi()
                    }
                    false->{
                        Log.d(TAG,"서치뷰 닫힘")
                        binding.linearSearchHistoryView.visibility=View.INVISIBLE
                        binding.searchRecycler.visibility=View.VISIBLE
                    }

                }
            }

            mySearchViewEditText=this.findViewById(androidx.appcompat.R.id.search_src_text)
        }
        this.mySearchViewEditText.apply{
            this.filters=arrayOf(InputFilter.LengthFilter(12))
            this.setTextColor(Color.BLACK)
            this.setHintTextColor(Color.BLACK)
        }
        return true
    }

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
    override fun onQueryTextSubmit(query: String?): Boolean {
        searchKeyword(query!!)
        if(!query.isNullOrEmpty()){
            insertSearchTermHistory(query)
        }
        this.mySearchView.setQuery("",false)
        this.mySearchView.clearFocus()
        this.topappbar.collapseActionView()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val userInputText=newText?:""
        if(userInputText.count()==12){
            Toast.makeText(this,"검색어는 12자 까지만 입력 가능합니다",Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onCheckedChanged(switch: CompoundButton?, isChecked: Boolean) {
        when(switch){
            search_history_mode_switch->{
                if(isChecked==true){
                    Log.d(TAG,"검색어 저장기능 on")
                    SharedPrefManager.setSearchHistoryMode(isActivated = true)
                }
                else{
                    Log.d(TAG,"검색어 저장기능 off")
                    SharedPrefManager.setSearchHistoryMode(isActivated = false)
                }
            }
        }
    }

    override fun onClick(view: View?) {
      when(view){
          clear_search_history_button->{
              Log.d(TAG,"검색 기록 삭제버튼클릭되어짐")
              SharedPrefManager.clearSearchHistoryList()
              this.searchHistoryList.clear()
              this.historyItems.clear()
              handleSearchViewUi()
          }
      }
    }
    private fun searchKeyword(keyword: String) {
        RetrofitManager.instance.searchPOI(searchpoi = keyword, completion = { responseState, response->
            when(responseState){
                RESPONSESTATE.OKAY->addItemsAndMarkers(response)
            }
        })
    }
    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        if (!searchResult?.searchPoiInfo?.pois?.poi.isNullOrEmpty()) {
// 검색 결과 있음
            listItems.clear() // 리스트 초기화
            for (document in searchResult!!.searchPoiInfo?.pois?.poi) {
// 결과를 리사이클러 뷰에 추가
                val item = ListLayout(
                    document.name,
                    document.upperAddrName,
                    document.middleAddrName,
                    document.lowerAddrName,
                    document.detailAddrName,
                    document.noorLat,
                    document.noorLon
                )
                listItems.add(item)
                listAdapter.notifyDataSetChanged()
            }
        }
        else {
// 검색 결과 없음
            Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }
    //검색 삭제 버튼 클릭
    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchItemDeleteClicked(position: Int) {
        Log.d(TAG,"onSearchItemDeleteClicked called")
        this.searchHistoryList.removeAt(position)
        this.historyItems.removeAt(position)
        SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)
        this.HistoryAdapter.notifyDataSetChanged()
        handleSearchViewUi()
    }
    //검색 아이템 버튼 클릭
    override fun onSearchItemClicked(position: Int) {
        Log.d(TAG,"onSearchItemClicked called")
        val keyword=this.historyItems[position].searchdata
        searchKeyword(keyword)
        this.mySearchView.setQuery("",false)
        this.mySearchView.clearFocus()
        this.topappbar.collapseActionView()
        insertSearchTermHistory(keyword)
    }
    private fun handleSearchViewUi(){
        Log.d(TAG,"handleSearchViewUi called / size : ${this.searchHistoryList.size}")
        if(this.searchHistoryList.size>0){
            binding.searchHistoryRecycler.visibility=View.VISIBLE
            binding.searchHistoryRecyclerLabel.visibility=View.VISIBLE
            binding.clearSearchHistoryButton.visibility=View.VISIBLE
        }
        else{
            binding.searchHistoryRecycler.visibility=View.INVISIBLE
            binding.searchHistoryRecyclerLabel.visibility=View.INVISIBLE
            binding.clearSearchHistoryButton.visibility=View.INVISIBLE
        }
    }
    //검색어 저장
    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    private fun insertSearchTermHistory(searchTerm:String){
        if(SharedPrefManager.checkSearchHistoryMode()==true){

            val indexListToRemove = ArrayList<Int>()
            this.searchHistoryList.forEachIndexed{index,searchdataItem->
                if(searchdataItem.searchdata==searchTerm){
                    indexListToRemove.add(index)
                }
            }
            indexListToRemove.forEach{
                this.searchHistoryList.removeAt(it)
                this.historyItems.removeAt(it)
            }
            val newSearchData=SearchData(searchdata = searchTerm, timestamp = SimpleDateFormat("HH:mm:ss").format(Date()).toString())
            this.searchHistoryList.add(newSearchData)
            this.historyItems.add(newSearchData)
            this.HistoryAdapter.notifyDataSetChanged()
            SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)
        }
    }

}