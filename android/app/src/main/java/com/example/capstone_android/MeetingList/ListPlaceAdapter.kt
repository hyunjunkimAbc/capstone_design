package com.example.capstone_android.MeetingList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.R
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.PlaceData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.itemplacelist.view.*

class ListPlaceAdapter(var itemlist : List<PlaceData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.itemplacelist,parent,false)
        return CustomPlaceViewHolder(view)
    }
    inner class CustomPlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val placeholder=(holder as CustomPlaceViewHolder).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
        Glide.with(holder.itemView.context)
            .load(itemlist[position].imageUrl)
            .placeholder(R.drawable.loadingicon)
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .error(R.drawable.plusprofileimg)
            .dontAnimate()
            .into(placeholder.recycleritemimg)
        placeholder.recycleritemplacetitle.text=itemlist[position].title
        placeholder.recycleritemgoodcount.text=itemlist[position].num_of_positive.toString()
        placeholder.recycleritemplaceexplain.text=itemlist[position].info_text
        placeholder.recycleritemaddress.text=itemlist[position].addressdetail
        placeholder.placeitemcategory.text=itemlist[position].category
        placeholder.placeclickitem.setOnClickListener{
            placeitemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
       return itemlist.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun subPlaceList(list: List<PlaceData>) {
        // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    interface PlaceItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setPlaceItemClickListener(onItemClickListener: PlaceItemClickListener) {
        this.placeitemClickListener = onItemClickListener
    }

    private lateinit var placeitemClickListener : PlaceItemClickListener

}