package com.example.capstone_android.MainHomeActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.capstone_android.MainActivity
import com.example.capstone_android.R
import com.example.capstone_android.data.BannerItem
import kotlinx.android.synthetic.main.item_layout_banner.view.*

class ViewPagerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val ITEM_COUNT = 4
    }

    private var bannerItemList: List<BannerItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_banner, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bannerItemList?.let { bannerItemList ->
            (holder as BannerViewHolder).bind(bannerItemList[position])
        }
    }

    //functions
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<BannerItem>?) {
        bannerItemList = list
        notifyDataSetChanged()
    }


    //ViewHolder
    class BannerViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bannerItem: BannerItem) { itemView.iv_banner_image.setImageResource(bannerItem.image) }
    }
}