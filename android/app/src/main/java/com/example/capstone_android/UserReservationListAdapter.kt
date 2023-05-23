package com.example.capstone_android

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.ItemUserReservationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class UserReservationListAdapter(private val viewModel: ReservationViewModel) :
    RecyclerView.Adapter<UserReservationListAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemUserReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setContents(pos: Int) {
            with(viewModel.items[pos]) {
                binding.placeImage.setImageBitmap(this.placeImage)
                binding.placeName.text = this.placeName
                binding.requestTodayText.text =
                    SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.requestTime)
                binding.reservatorName.text = this.reservatorName
                binding.numOfPeopleText.text = this.numOfPeople.toString() + "명"
                binding.reservationDate.text = this.requestDate
                binding.reservationSchedule.text =
                    "${this.startReservedSchedule}시~${this.endReservedSchedule}시"

                if (this.okCheck) {
                    binding.okcheck.text = "예약승인완료"
                    binding.okcheck.setTextColor(Color.BLUE)
                } else {
                    binding.okcheck.text = "승인 대기중"
                    binding.okcheck.setTextColor(Color.RED)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserReservationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    override fun getItemCount() = viewModel.items.size
}