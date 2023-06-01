package com.example.capstone_android.Alarma

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.Alarm.Alarmdata
import com.example.capstone_android.R
import com.example.capstone_android.data.ClubData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

import kotlinx.android.synthetic.main.item_alarm.view.*
import kotlinx.android.synthetic.main.item_main.view.*
import java.text.SimpleDateFormat
import java.util.*

class UserAlarmAdapter(var itemlist : List<Alarmdata>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_alarm,parent,false)
        return AlarmViewHolder(view)
    }
    inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewholder=(holder as AlarmViewHolder).itemView
        viewholder.alarmtext.text=itemlist[position].username.plus(" ").plus(itemlist[position].message)
        val timestampString = itemlist[position].timestamp.toLong()
        val date = Date(timestampString)
        val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.getDefault())
        val formattedDate = outputFormat.format(date)

        viewholder.alarmtime.text=formattedDate
        Glide.with(holder.itemView.context)
            .load(itemlist[position].imageUrl)
            .apply(RequestOptions().transform(CenterCrop(),CircleCrop()))
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.plusprofileimg)
            .dontAnimate()
            .into(viewholder.alarmimg)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun subalarm(list: List<Alarmdata>) {
         Log.d(TAG,"${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return itemlist.size
    }
}