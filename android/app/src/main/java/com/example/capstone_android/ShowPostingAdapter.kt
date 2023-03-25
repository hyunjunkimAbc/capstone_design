package com.example.capstone_android


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.MeetingRoomPostingCommentItemLayoutBinding
import com.example.capstone_android.databinding.MeetingRoomPostingCommentItemRightBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class ShowPostingAdapter (private val viewModel: ShowPostingViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(private val binding: MeetingRoomPostingCommentItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun setContents(pos : Int){
            with(viewModel.items[pos]){
                if(this.icon!=null)
                    binding.profileImgPostingComment.setImageBitmap(this.icon)
                //null들어가면 작동 안함 주의
                binding.nicknamePostingComment.text = this.nickname
                binding.timePostingComment.text = "${SimpleDateFormat("yyyy-MM-dd").format(this.timePosting)}"
                binding.textPostingComment.text = this.commentText
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
    inner class ViewHolderRight(private val binding: MeetingRoomPostingCommentItemRightBinding): RecyclerView.ViewHolder(binding.root){
        fun setContents(pos : Int){
            with(viewModel.items[pos]){
                //null들어가면 작동 안함 주의
                binding.timePostingCommentRight.text = "${SimpleDateFormat("yyyy-MM-dd").format(this.timePosting)}"
                binding.textPostingCommentRight.text = this.commentText
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

    override fun getItemViewType(position: Int): Int {
        if(viewModel.items[position].writer_uid != Firebase.auth.uid){
            return 0
        }else{
            return 1
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType ==0){
            val layoutInflater = LayoutInflater.from(viewGroup.context)
            val binding = MeetingRoomPostingCommentItemLayoutBinding.inflate(layoutInflater,viewGroup,false)
            return ViewHolder(binding)

        }else{
            val layoutInflater = LayoutInflater.from(viewGroup.context)
            val binding = MeetingRoomPostingCommentItemRightBinding.inflate(layoutInflater,viewGroup,false)
            return ViewHolderRight(binding)
        }
    }
    /*
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setContents(position)
    }*/

    override fun getItemCount()=viewModel.items.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolderRight){
            holder.setContents(position)
        }else if(holder is ViewHolder){
            holder.setContents(position)
        }
    }

}