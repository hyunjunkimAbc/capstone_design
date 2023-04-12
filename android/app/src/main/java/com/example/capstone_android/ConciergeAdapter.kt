package com.example.capstone_android

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.ConciergeItemLayoutBinding

class ConciergeAdapter (private val viewModel: ConciergeViewModel): RecyclerView.Adapter<ConciergeAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ConciergeItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun setContents(pos : Int){
            with(viewModel.items[pos]){
                if(this.icon!=null)
                    binding.conciergeImageView.setImageBitmap(this.icon)
                //null들어가면 작동 안함 주의
                binding.conciergeTextView.text =this.title
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
        val binding = ConciergeItemLayoutBinding.inflate(layoutInflater,viewGroup,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setContents(position)
    }

    override fun getItemCount()=viewModel.items.size

}