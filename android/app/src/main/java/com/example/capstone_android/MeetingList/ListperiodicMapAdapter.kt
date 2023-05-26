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
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.ClubData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.itemmapfragment.view.*


class ListperiodicMapAdapter(var itemlist : List<ClubData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.itemmapfragment,parent,false)
        return CustomViewHoldermap(view)
    }
    inner class CustomViewHoldermap(view: View) : RecyclerView.ViewHolder(view) {

    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewholder=(holder as CustomViewHoldermap).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(16, 5)
        Glide.with(holder.itemView.context).load(itemlist[position].imageUrl).transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .override(100, 100)
            .into(viewholder.imageViewmap)
        viewholder.mapaddress.text= itemlist[position].address
        viewholder.mapmember.text= itemlist[position].member_list?.size.toString()
        viewholder.mapclubName.text= itemlist[position].title
        viewholder.category.text= itemlist[position].category
        viewholder.mapexplain.text= itemlist[position].info_text
        viewholder.mapclick.setOnClickListener{
            periodicMapItemClickListener.onClick(it,position)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submapitemlist(list: List<ClubData>) {
        // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return itemlist.size
    }
    interface PeriodicMapItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setPeriodicMapItemClickListener(onItemClickListener: PeriodicMapItemClickListener) {
        this.periodicMapItemClickListener = onItemClickListener
    }

    private lateinit var periodicMapItemClickListener : PeriodicMapItemClickListener

}