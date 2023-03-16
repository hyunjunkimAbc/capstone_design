package com.example.capstone_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.data.AddressData
import com.example.capstone_android.databinding.ActivityAddressBinding
import jxl.Workbook
import jxl.read.biff.BiffException
import kotlinx.android.synthetic.main.addressitem.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.io.IOException


class AddressActivity:AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    var itemlist:ArrayList<AddressData> = arrayListOf()
    var searchlist:ArrayList<AddressData> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val addressrecycler=binding.addressrecyclerview
        val search=binding.searchicon
        search.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.textview.text="클럽 지역"

        try {
            val `is` = baseContext.resources.assets.open("address.xls")
            val wb = Workbook.getWorkbook(`is`)
            if (wb != null) {
                val sheet = wb.getSheet(0) // 시트 불러오기
                if (sheet != null) {
                    val colTotal = sheet.columns // 전체 컬럼
                    val rowIndexStart = 1 // row 인덱스 시작
                    val rowTotal = sheet.getColumn(colTotal - 1).size
                    var sb: StringBuilder
                    for (row in rowIndexStart until rowTotal) {
                        val item= AddressData()
                        item.first=sheet.getCell(0,row).contents
                        item.second=sheet.getCell(1,row).contents
                        item.third=sheet.getCell(2,row).contents
                        if(item.third!="") {
                            itemlist.add(item)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("?wkfaht잘못됫어")
        } catch (e: BiffException) {
            e.printStackTrace()
            println("?wkfaht")
        }
        addressrecycler.adapter=SearchAddressAdapter()
        addressrecycler.layoutManager= LinearLayoutManager(this)
        search.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchtext:String=binding.searchicon.text.toString()
                searchlist.clear()
                if(searchtext.equals("")){
                    searchlist.clear()
                    (addressrecycler.adapter as SearchAddressAdapter).notifyDataSetChanged()
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable?) {
              val searchtext:String=binding.searchicon.text.toString()
                searchlist.clear()
                if(searchtext.equals("")){
                    searchlist.clear()
                    (addressrecycler.adapter as SearchAddressAdapter).notifyDataSetChanged()
                }
                for((index,data) in itemlist.withIndex()){
                    if(itemlist[index].first!!.contains(searchtext)||itemlist[index].second!!.contains(searchtext)||itemlist[index].third!!.contains(searchtext)){
                        searchlist.add(itemlist[index])
                    }
                    (addressrecycler.adapter as SearchAddressAdapter).notifyDataSetChanged()
                }
            }

        })
    }
    inner class SearchAddressAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        private var items: ArrayList<AddressData> = ArrayList<AddressData>()
        init{

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.addressitem,parent,false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewholder=(holder as AddressActivity.SearchAddressAdapter.CustomViewHolder).itemView
           viewholder.subaddress.text=searchlist[position].third
            val explain:String="(".plus(searchlist[position].first).plus(" ").plus(searchlist[position].second).plus(")")
            viewholder.address.text=explain
        }

        override fun getItemCount(): Int {
            return searchlist.size
        }
    }

}