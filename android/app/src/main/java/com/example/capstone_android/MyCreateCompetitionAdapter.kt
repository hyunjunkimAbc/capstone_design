package com.example.capstone_android

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.CompetitionRoomItemBinding
import java.text.SimpleDateFormat

class MyCreateCompetitionAdapter (private val viewModel: MyCreateCompetitionViewModel): RecyclerView.Adapter<MyCreateCompetitionAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CompetitionRoomItemBinding): RecyclerView.ViewHolder(binding.root){
        fun setContents(pos : Int){
            with(viewModel.items[pos]){
                if(this.icon!=null)
                    binding.imageviewItemImage.setImageBitmap(this.icon)
                //null들어가면 작동 안함 주의
                binding.textviewItemTitle.text = this.title
                binding.textviewItemUploadTime.text = "${SimpleDateFormat("yyyy-MM-dd").format(this.uploadTime)}"
                binding.textviewItemInformation.text = this.infoText
                //친구 기능 추가 하고 싶으면  지난 학기에 코딩한 github 참고
                android.util.Log.d("","${ this.title}")

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
        val binding = CompetitionRoomItemBinding.inflate(layoutInflater,viewGroup,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setContents(position)
    }

    override fun getItemCount()=viewModel.items.size


}