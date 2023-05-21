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
import kotlinx.android.synthetic.main.itemcompetitionmaplist.view.*

class ListCompetitionMapAdapter(var itemlist: List<CompetitionData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.itemcompetitionmaplist,parent,false)
        return CustomCompetitionMapViewHolder(view)
    }
    inner class CustomCompetitionMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val competitionmapholder=(holder as CustomCompetitionMapViewHolder).itemView
        val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
        Glide.with(holder.itemView.context)
            .load(itemlist[position].imageUrl)
            .placeholder(R.drawable.loadingicon)
            .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
            .error(R.drawable.plusprofileimg)
            .dontAnimate()
            .into(competitionmapholder.competitionmapimg)
        competitionmapholder.competitionmapgoodcount.text=itemlist[position].num_of_positive.toString()
        competitionmapholder.competitionmapexpain.text=itemlist[position].info_text
        competitionmapholder.competitionmapstart.text="시작: "+itemlist[position].start_time
        competitionmapholder.competitionmapend.text="종료: "+itemlist[position].end_time
        competitionmapholder.competitionmapitemclick.setOnClickListener{
            competitionmapitemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun subCompetitionMapList(list: List<CompetitionData>) {
        // Log.d(TAG," ${list.size}")
        itemlist = list
        notifyDataSetChanged()
    }
    interface CompetitionMapItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setCompetitionMapItemClickListener(onItemClickListener: CompetitionMapItemClickListener) {
        this.competitionmapitemClickListener = onItemClickListener
    }

    private lateinit var competitionmapitemClickListener : CompetitionMapItemClickListener

}