package com.example.capstone_android

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.MeetingRoomInfoItemLayoutBinding
import com.example.capstone_android.databinding.MeetingRoomPostingsItemLayoutBinding
import java.text.SimpleDateFormat

class MeetingRoomPostingsAdapter (private val viewModel: MeetingRoomPostingsViewModel): RecyclerView.Adapter<MeetingRoomPostingsAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: MeetingRoomPostingsItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun setContents(pos : Int){
            with(viewModel.items[pos]){
                if(this.icon!=null)
                    binding.profileImgPosting.setImageBitmap(this.icon)
                //null들어가면 작동 안함 주의
                binding.postTitle.text = this.title
                binding.nicknamePosting.text = this.nickname
                binding.timePosting.text = "${SimpleDateFormat("yyyy-MM-dd").format(this.timePosting)}"
                binding.textPosting.text = this.postingText
                //친구 기능 추가 하고 싶으면  지난 학기에 코딩한 github 참고
                Log.d("","${ this.nickname}")

            }

            binding.root.setOnClickListener {
                viewModel.itemClickEvent.value = adapterPosition
            }
            binding.root.setOnLongClickListener {
                viewModel.itemLongClick = adapterPosition
                false
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = MeetingRoomPostingsItemLayoutBinding.inflate(layoutInflater,viewGroup,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setContents(position)
    }

    override fun getItemCount()=viewModel.items.size

}