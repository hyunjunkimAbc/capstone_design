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
import com.example.capstone_android.data.lightData

import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.itemmaplightitem.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListLightMapAdapter(var itemlist: List<lightData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.itemmaplightitem,parent,false)
        return CustomViewHoldermap(view)
    }
    inner class CustomViewHoldermap(view: View) : RecyclerView.ViewHolder(view) {

    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewholder=(holder as CustomViewHoldermap).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(16, 5)
        Glide.with(holder.itemView.context).load(itemlist[position].imageUrl).transition(
            DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .override(100, 100)
            .into(viewholder.lightmapitemimageViewmap)
        viewholder.lightmapitemaddress.text= itemlist[position].address
        viewholder.lightmapitemaddressname.text=itemlist[position].addressname
        viewholder.lightmapitemclubName.text= itemlist[position].title
        viewholder.lightmapitemcategory.text= itemlist[position].category
        viewholder.lightmapitemexplain.text= itemlist[position].info_text
        val currentTime : Long = System.currentTimeMillis()
        val dataFormat1 = SimpleDateFormat("yyyyMMdd")
        val nowtime=dataFormat1.format(currentTime).toInt()
        val meetingtime=itemlist[position].date?.toInt()
        val dday=meetingtime!!-nowtime
        val cal: Calendar = Calendar.getInstance()
        var strWeek: String? = null
        var nWeek: Int = cal.get(Calendar.DAY_OF_WEEK)+dday
        if(nWeek>7){
            while(nWeek>7){
                nWeek-=7
            }
        }
        if (nWeek == 1) {
            strWeek = "일"
        } else if (nWeek == 2) {
            strWeek = "월"
        } else if (nWeek == 3) {
            strWeek = "화"
        } else if (nWeek == 4) {
            strWeek = "수"
        } else if (nWeek == 5) {
            strWeek = "목"
        } else if (nWeek == 6) {
            strWeek = "금"
        } else if (nWeek == 7) {
            strWeek = "토"
        }
        viewholder.lightmapitemdday.text="D-".plus(dday).plus(strWeek)
        viewholder.lightmapitemstarttime.text=itemlist[position].start_time2
        viewholder.lightmapitemend.text=itemlist[position].end_time2
        viewholder.lightmapclick.setOnClickListener{
            lightMapItemClickListener.onClick(it,position)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submaplightitem(list: List<lightData>) {
        // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return itemlist.size
    }

    interface LightMapItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setLightMapItemClickListener(onItemClickListener: LightMapItemClickListener) {
        this.lightMapItemClickListener = onItemClickListener
    }

    private lateinit var lightMapItemClickListener : LightMapItemClickListener

}