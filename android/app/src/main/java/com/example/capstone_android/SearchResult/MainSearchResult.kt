package com.example.capstone_android.SearchResult

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivityMainsearchresultBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainSearchResult:AppCompatActivity(),android.widget.SearchView.OnQueryTextListener  {
    private lateinit var binding: ActivityMainsearchresultBinding
    private var searchdata: String? = null
    private val tabTitleArray = arrayOf(
        "모임",
        "번개모임",
        "장소",
        "대외활동"
    )
    var MainSearchViewExpaned:android.widget.SearchView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainsearchresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar=binding.searchresulttoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        searchdata = intent.getStringExtra("searchdata")
        if (!searchdata.isNullOrEmpty()) {
            binding.searchresultsearch.setQuery(searchdata, false)
        }
        binding.searchresultsearch.setQuery(searchdata,false)
        MainSearchViewExpaned=binding.searchresultsearch
        MainSearchViewExpaned?.clearFocus()
        MainSearchViewExpaned?.setOnQueryTextListener(this)
        val viewpager=binding.searchresultviewpager
        val tabLayout = binding.searchtab
        viewpager.adapter=SearchViewPagerAdapter(supportFragmentManager,lifecycle,searchdata!!)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()) {
            var intent = Intent(this, MainSearchResult::class.java)
            intent.putExtra("searchdata", query)
            startActivity(intent)
            finish()
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


}