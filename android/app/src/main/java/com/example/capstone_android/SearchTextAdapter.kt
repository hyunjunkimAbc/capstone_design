package com.example.capstone_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.`interface`.SearchHistoryRecyclerview
import com.example.capstone_android.data.ListLayout
import com.example.capstone_android.data.SearchData

class SearchTextAdapter(val itemList: ArrayList<ListLayout>) : RecyclerView.Adapter<SearchTextAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTextAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searchaddressitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SearchTextAdapter.ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.address.text = itemList[position].upperAddrName.plus(itemList[position].middleAddrNmae).plus(itemList[position].lowerAddrName).plus(itemList[position].detailAddrName)
// 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_list_name)
        val road: TextView = itemView.findViewById(R.id.tv_list_road)
        val address: TextView = itemView.findViewById(R.id.tv_list_address)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}