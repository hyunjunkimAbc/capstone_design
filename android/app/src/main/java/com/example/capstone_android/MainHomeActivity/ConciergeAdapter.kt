package com.example.capstone_android.MainHomeActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.ConciergeItemLayoutBinding

open class ConciergeAdapter (private val viewModel: ConciergeViewModel): RecyclerView.Adapter<ConciergeAdapter.ViewHolder>(){
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
                println("click-------------------")
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
open class LightingAdapter (private val viewModel: LightingViewModel): ConciergeAdapter(viewModel){
}
open class PeriodicAdapter (private val viewModel: PeriodicViewModel): ConciergeAdapter(viewModel){

}
open class PlaceRentalAdapter (private val viewModel: PlaceRentalViewModel): ConciergeAdapter(viewModel){

}
open class CompetitionAdapter (private val viewModel: CompetitionViewModel): ConciergeAdapter(viewModel){

}
