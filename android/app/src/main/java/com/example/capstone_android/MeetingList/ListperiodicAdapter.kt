package com.example.capstone_android.MeetingList

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

import com.example.capstone_android.R
import com.example.capstone_android.data.ClubData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_main.view.*

class ListperiodicAdapter(var itemlist : List<ClubData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_main,parent,false)
        return CustomViewHolder(view)

    }
    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    @SuppressLint("CheckResult", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewholder=(holder as CustomViewHolder).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
        Log.d(TAG,"몇번호출?" )
        Glide.with(holder.itemView.context)
            .load(itemlist[position].imageUrl)
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .override(80, 80)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.plusprofileimg)
            .dontAnimate()
            .into(viewholder.detailviewitem_imageview_content)
        viewholder.itemClubName.text= itemlist[position].title
        viewholder.itemusercount.text="멤버 "+ itemlist[position].member_list?.size
        viewholder.itemaddress.text= itemlist[position].address
        viewholder.itemmaincategory.text = itemlist[position].category
        viewholder.itemClubExplain.text= itemlist[position].info_text
        viewholder.CardView.setOnClickListener{
            meetingitemClickListener.onClick(it, position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submmeetingitList(list: List<ClubData>) {
       // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    interface MeetingItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: MeetingItemClickListener) {
        this.meetingitemClickListener = onItemClickListener
    }

    private lateinit var meetingitemClickListener : MeetingItemClickListener

    override fun getItemCount(): Int {
        return itemlist.size
    }

}