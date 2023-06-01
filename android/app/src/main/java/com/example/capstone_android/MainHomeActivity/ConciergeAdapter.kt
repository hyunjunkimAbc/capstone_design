package com.example.capstone_android.MainHomeActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.R
import com.example.capstone_android.databinding.ConciergeItemLayoutBinding
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.withContext

open class ConciergeAdapter (private val viewModel: ConciergeViewModel): RecyclerView.Adapter<ConciergeAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ConciergeItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun setContents(pos : Int){
            with(viewModel.items[pos]){
                /*
                if(this.icon!=null)
                    binding.conciergeImageView.setImageBitmap(this.icon)*/
                //null들어가면 작동 안함 주의
                binding.conciergeTextView.text =this.title
                binding.conciergeInfoText.text = this.address
                binding.conciergecategory.text = this.category
                binding.conciergeInfoText2.text=this.info_text
                val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
                Glide.with(this.context)
                    .load(this.imageUrl)
                    .apply(RequestOptions().transform(CenterCrop(), MultiTransformation(listOf(roundedCornersTransformation))))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.plusprofileimg)
                    .dontAnimate()
                    .into(binding.conciergeImageView)
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
