package com.example.capstone_android.MeetingList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.R
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.PlaceData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.itemplacelist.view.*
import kotlinx.android.synthetic.main.itemplacemaplist.view.*

class ListPlaceMapAdapter(var itemlist : List<PlaceData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.itemplacemaplist,parent,false)
        return CustomPlaceMapViewHolder(view)
    }
    inner class CustomPlaceMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val placemapholder=(holder as CustomPlaceMapViewHolder).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
        Glide.with(holder.itemView.context)
            .load(itemlist[position].imageUrl)
            .placeholder(R.drawable.loadingicon)
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .error(R.drawable.plusprofileimg)
            .dontAnimate()
            .into(placemapholder.recycleritemmapimg)
        placemapholder.recycleritemmaptitle.text=itemlist[position].title
        placemapholder.placemapaddress.text=itemlist[position].addressdetail
        placemapholder.placemapcategory.text=itemlist[position].category
        placemapholder.recycleritemmapgoodcount.text=itemlist[position].num_of_positive.toString()
        placemapholder.placemapitemclick.setOnClickListener{
            placemapitemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun subplacemap(list: List<PlaceData>) {
        // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    interface PlaceMapItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setPlaceItemClickListener(onItemClickListener: PlaceMapItemClickListener) {
        this.placemapitemClickListener = onItemClickListener
    }

    private lateinit var placemapitemClickListener : PlaceMapItemClickListener
}