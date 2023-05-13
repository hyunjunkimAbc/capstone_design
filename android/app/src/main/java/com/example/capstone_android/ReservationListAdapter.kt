package com.example.place_rental

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.ItemReservationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReservationListAdapter(private val viewModel: ReservationViewModel):
    //(private val reservations: List<ReservationListData>) :
    RecyclerView.Adapter<ReservationListAdapter.ViewHolder>() {
    //var reservationHistory /*: ArrayList<ReservationListData>*/ = reservations
    inner class ViewHolder(private val binding: ItemReservationBinding):RecyclerView.ViewHolder(binding.root){
        var txt:String = ""
        val db = Firebase.firestore
        fun setContents(pos : Int){
            with(viewModel.items[pos]){
                binding.placeImage.setImageBitmap(this.placeImage)
                binding.placeName.text = this.placeName
                binding.requestTodayText.text = this.requestToday
                binding.reservatorName.text = this.reservatorName
                binding.numOfPeopleText.text = this.numOfPeople.toString()+"명"
                binding.reservationDate.text = this.requestDate
                binding.reservationSchedule.text = "${this.startReservedSchedule}시~${this.endReservedSchedule}시"

                setBtn(this.okCheck)

                binding.reserOkBtn.setOnClickListener(){
                    this.okCheck = !okCheck
                    setBtn(this.okCheck)
                    db.collection("reservation").document(this.reserDocument!!)
                        .update("okCheck", this.okCheck)
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
        private fun setBtn(okCheck:Boolean){
            if(okCheck) {
                binding.okcheck.text = "O"
                binding.reserOkBtn.setText("예약취소")
                binding.reserOkBtn.setTextColor(Color.RED)
            } else {
                binding.okcheck.text = "X"
                binding.reserOkBtn.setText("예약승인")
                binding.reserOkBtn.setTextColor(Color.BLUE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    override fun getItemCount() = viewModel.items.size
}