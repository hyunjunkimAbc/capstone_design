package com.example.capstone_android.SearchAddress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.R
import com.example.capstone_android.SearchAddress.SearchHistoryRecyclerview
import com.example.capstone_android.data.ListLayout
import com.example.capstone_android.data.SearchData
import kotlinx.android.synthetic.main.search_shared_history_item.view.*

class SearchTextHistoryAdapter(val itemList: ArrayList<SearchData>,searchHistoryRecyclerviewInterface: SearchHistoryRecyclerview): RecyclerView.Adapter<SearchTextHistoryAdapter.SearchItemViewHoler>()  {
    //private var searchHistoryList:ArrayList<SearchData> = ArrayList()
    private var searchHistoryrecyclerView:SearchHistoryRecyclerview?=null
    init{
        this.searchHistoryrecyclerView=searchHistoryRecyclerviewInterface
    }

    class SearchItemViewHoler(itemView: View,searchRecyclerInterface:SearchHistoryRecyclerview): RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private lateinit var mySearchHistoryRecyclerViewInterface:SearchHistoryRecyclerview
        private val searchTermTextView=itemView.search_term_text
        private val whenSearchedTextView=itemView.when_searched_text
        private val deleteSearchButton=itemView.delete_search_button
        private val constraintSearchitem=itemView.constraint_search_item
        init{
            deleteSearchButton.setOnClickListener(this)
            constraintSearchitem.setOnClickListener(this)
            this.mySearchHistoryRecyclerViewInterface=searchRecyclerInterface
        }
        fun bindWithView(searchItem:SearchData){
            searchTermTextView.text=searchItem.searchdata
        whenSearchedTextView.text=searchItem.timestamp
        }
        override fun onClick(view: View?) {
            when(view){
                deleteSearchButton->{
                this.mySearchHistoryRecyclerViewInterface.onSearchItemDeleteClicked(adapterPosition)
                }
                constraintSearchitem->{
                this.mySearchHistoryRecyclerViewInterface.onSearchItemClicked(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHoler {
        val view = SearchItemViewHoler(LayoutInflater.from(parent.context).inflate(R.layout.search_shared_history_item, parent, false),this.searchHistoryrecyclerView!!)
        return view
    }

    override fun onBindViewHolder(holder: SearchItemViewHoler, position: Int) {
      holder.bindWithView(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}