package com.example.capstone_android.SearchResult

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.R
import com.example.capstone_android.SearchAddress.SearchHistoryRecyclerview
import com.example.capstone_android.data.PlaceData
import com.example.capstone_android.data.SearchData
import kotlinx.android.synthetic.main.itemsearchitem.view.*

import kotlinx.android.synthetic.main.search_shared_history_item.view.*

class MainSearchAdapter(var itemList: List<SearchData>, searchHistoryRecyclerviewInterface: SearchHistoryRecyclerview) : RecyclerView.Adapter<MainSearchAdapter.MainSearchItemViewHoler>() {

    private var searchHistoryrecyclerView:SearchHistoryRecyclerview?=null
    init{
        this.searchHistoryrecyclerView=searchHistoryRecyclerviewInterface
    }

    class MainSearchItemViewHoler(itemView: View, searchRecyclerInterface:SearchHistoryRecyclerview): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private lateinit var mySearchHistoryRecyclerViewInterface:SearchHistoryRecyclerview
        private val searchtext=itemView.searchrecyclertext
        private val clickitem=itemView.clicksearchitem
        private val deleteitem=itemView.searchrecyclerremove
        init{
            deleteitem.setOnClickListener(this)
            clickitem.setOnClickListener(this)
            this.mySearchHistoryRecyclerViewInterface=searchRecyclerInterface
        }
        fun bindWithView(searchItem:SearchData){
            searchtext.text=searchItem.searchdata
        }
        override fun onClick(view: View?) {
            when(view){
                deleteitem->{
                    this.mySearchHistoryRecyclerViewInterface.onSearchItemDeleteClicked(adapterPosition)
                }
                clickitem->{
                    this.mySearchHistoryRecyclerViewInterface.onSearchItemClicked(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainSearchItemViewHoler {
        val view = MainSearchItemViewHoler(LayoutInflater.from(parent.context).inflate(R.layout.itemsearchitem, parent, false), this.searchHistoryrecyclerView!!)
        return view
    }

    override fun onBindViewHolder(holder: MainSearchItemViewHoler, position: Int) {
        holder.bindWithView(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submainsearch(list: ArrayList<SearchData>) {
        // Log.d(TAG," ${list.size}")
        itemList = list
        notifyDataSetChanged()
    }
}