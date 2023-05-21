package com.example.capstone_android.MeetingList

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.R
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.lightData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.itemlight.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListLightAdapter(var itemlist:List<lightData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.itemlight,parent,false)
        return CustomViewHolder2(view)
    }
    inner class CustomViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

    }
    @SuppressLint("CheckResult", "SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewholder=(holder as CustomViewHolder2).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
        Glide.with(holder.itemView.context)
            .load(itemlist[position].imageUrl)
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .override(75, 75)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.plusprofileimg)
            .dontAnimate()
            .into(viewholder.lightitemimg)
        viewholder.lightitemtitle.text=itemlist[position].title
        viewholder.lightitemnumber.text=itemlist[position].max.plus("명")
        viewholder.lightitemcategory.text=itemlist[position].category
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

        viewholder.lightitemdday.text= "D-".plus(dday).plus(strWeek)
        viewholder.lightitemstart.text= itemlist[position].start_time
        viewholder.lightitemend.text=itemlist[position].end_time
        viewholder.lightitemaddress.text=itemlist[position].address
        viewholder.lightitemaddressname.text=itemlist[position].addressname
        viewholder.lightclick.setOnClickListener{
            lightmeetingitemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun sublightmmeetingitList(list:List<lightData>) {
        // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    interface LightMeetingItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: LightMeetingItemClickListener) {
        this.lightmeetingitemClickListener = onItemClickListener
    }

    private lateinit var lightmeetingitemClickListener : LightMeetingItemClickListener
}