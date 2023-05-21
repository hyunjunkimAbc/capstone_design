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
import com.example.capstone_android.data.CompetitionData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.itemcompetitionlist.view.*
import kotlinx.android.synthetic.main.itemplacelist.view.*


class ListCompetitionAdapter(var itemlist: List<CompetitionData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.itemcompetitionlist,parent,false)
        return CustomCompetitionViewHolder(view)
    }
    inner class CustomCompetitionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val competitionholder=(holder as CustomCompetitionViewHolder).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
        Glide.with(holder.itemView.context)
            .load(itemlist[position].imageUrl)
            .placeholder(R.drawable.loadingicon)
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .error(R.drawable.plusprofileimg)
            .dontAnimate()
            .into(competitionholder.competitionitemimg)
        competitionholder.competitionitemtitle.text=itemlist[position].title
        competitionholder.competitioncategory.text=itemlist[position].category
        competitionholder.competitionitemgoodcount.text=itemlist[position].num_of_positive.toString()
        competitionholder.competitionitemexplain.text=itemlist[position].info_text
        competitionholder.competitionaddress.text=itemlist[position].location
        competitionholder.competitionitemstart.text=itemlist[position].start_time
        competitionholder.competitionitemend.text=itemlist[position].end_time
        competitionholder.competitionitemclick.setOnClickListener{
            competitionitemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
       return itemlist.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun subCompetitionList(list: List<CompetitionData>) {
        // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    interface CompetitionItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setCompetitionItemClickListener(onItemClickListener: CompetitionItemClickListener) {
        this.competitionitemClickListener = onItemClickListener
    }

    private lateinit var competitionitemClickListener : CompetitionItemClickListener
}