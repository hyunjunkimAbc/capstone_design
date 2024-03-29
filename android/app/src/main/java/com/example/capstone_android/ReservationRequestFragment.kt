package com.example.capstone_android

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.graphics.Color
import android.os.Build
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.capstone_android.R
import com.example.capstone_android.data.ReservationRequestData
import com.example.capstone_android.databinding.FragmentReservationBinding
import com.example.capstone_android.databinding.FragmentReservationRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ReservationRequestFragment: Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!
    var db : FirebaseFirestore = Firebase.firestore
    var stRef = Firebase.storage.reference
    var scheduleBtn : Array<Boolean> = arrayOf(false,false,false,false,false,false,false,false,false)

    var reserStart:Int=100
    var reserEnd:Int=0
    var max : Int? = null
    val placeUid:String=""

    //var place_id = activity?.intent?.getStringExtra("place_rental_room_id") ?: "9cAZZmIcuNFcGfoK5oen"
    var place_id =""

    var time : Long =0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        place_id = arguments?.getString("place_rental_room_id").toString()
        scheduleBtn = arrayOf(false,false,false,false,false,false,false,false,false)

        setImageView()
        db.collection("place_rental_room").document(place_id).get().addOnSuccessListener {
            binding.TitleTextView.text = it["title"] as String
            binding.subtitleTextView2.text = it["info_text"] as String
            binding.addressTextView2.text = it["address"] as String
        }
        var calendarView = binding.calendarView
        var currTime=  Calendar.getInstance().timeInMillis
        var selectTime = currTime
        val formatted = "${SimpleDateFormat("yyyy-MM-dd").format(currTime)}"

        var reservationDay:String?=formatted
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            selectTime = Calendar.getInstance().apply{
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.timeInMillis
            if(currTime>selectTime){
                Toast.makeText(activity, "해당 날짜는 예약할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                var month2 = if (month < 9) "0" + (month + 1).toString() else (month + 1).toString()
                var dayOfMonth2 =
                    if (dayOfMonth < 10) "0" + dayOfMonth.toString() else dayOfMonth.toString()
                reservationDay = "${year}-${month2}-${dayOfMonth2}"
            }
        }

        db.collection("place_rental_room").document(place_id).get()
            .addOnSuccessListener {
                max = it["max"].toString().toInt()
                binding.maxTextView.setText(max.toString())
            }

        binding.button.setOnClickListener(){
            setScheduleBtn(0)
        }
        binding.button2.setOnClickListener(){
            setScheduleBtn(1)
        }
        binding.button3.setOnClickListener(){
            setScheduleBtn(2)
        }
        binding.button4.setOnClickListener(){
            setScheduleBtn(3)
        }
        binding.button5.setOnClickListener(){
            setScheduleBtn(4)
        }
        binding.button6.setOnClickListener(){
            setScheduleBtn(5)
        }
        binding.button7.setOnClickListener(){
            setScheduleBtn(6)
        }
        binding.button8.setOnClickListener(){
            setScheduleBtn(7)
        }
        binding.button9.setOnClickListener(){
            setScheduleBtn(8)
        }

        binding.ReservationBtn.setOnClickListener(){ // 예약신청 버튼 클릭
            if(currTime>selectTime){
                Toast.makeText(activity, "날짜를 다시 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(!scheduleBtn.contains(true)){
                Toast.makeText(activity, "이용시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(binding.numOfPeopleEditText.getText().toString()==""){
                Toast.makeText(activity, "이용인원을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(binding.numOfPeopleEditText.getText().toString().toInt()>max!!){
                Toast.makeText(activity, "최대 인원을 초과했습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                reservationTime()
                time = System.currentTimeMillis()
                var reserData = ReservationRequestData()
                db.collection("user").document(Firebase.auth.uid.toString()).get()
                    .addOnSuccessListener {
                        //nickname = it["nickname"].toString()
                        reserData.reservatorName = it["nickname"].toString()
                        //장소 id를 받아야함
                        db.collection("place_rental_room").document(place_id).get()
                            .addOnSuccessListener {
                                reserData.placeName = it["title"].toString()
                                reserData.placeUid = it.id
                                reserData.reservatorUid = Firebase.auth.uid.toString()
                                reserData.requestDate = reservationDay
                                reserData.startReservedSchedule = reserStart.toString()
                                reserData.endReservedSchedule = reserEnd.toString()
                                reserData.numOfPeople = binding.numOfPeopleEditText.getText().toString().toInt()
                                //reserData.numOfPeople = 5
                                reserData.requestToday = formatted
                                reserData.requestTime = time
                                db.collection("reservation").document("${Firebase.auth.uid}${time}")
                                    .set(reserData)
                                db.collection("place_rental_room").document(place_id)
                                    .update(
                                        "reservation_uid_list",
                                        FieldValue.arrayUnion("${Firebase.auth.uid}${time}")
                                    )
                                db.collection("user").document("${Firebase.auth.uid}")
                                    .update(
                                        "reservation_list",
                                        FieldValue.arrayUnion("${Firebase.auth.uid}${time}")
                                    )
                            }
                        Toast.makeText(context, "예약신청이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                        findNavController().navigate(R.id.action_reservationRequestFragment_to_meetingRoomInfoFragment)
                            //.commit()
                        //requireActivity().supportFragmentManager.popBackStack()
                    }
            }
        }

    }
    private fun setImageView(){
        var placeImage = stRef.child("place_rental_room/${place_id}.jpg")
        placeImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                binding.PlaceImageView.setImageBitmap(bmp)
            }else{
                var ref = stRef.child("place_rental_room/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        binding.PlaceImageView.setImageBitmap(bmp)
                    }else{
                        println("undefined err")
                    }
                }
            }
        }
    }

    private fun reservationTime() {
        for (i: Int in 0..8) {
            if (scheduleBtn[i] == true) {
                calStartEndTime(i)
            }
        }
    }

    private fun calStartEndTime(i:Int){
        if(!(reserStart<=i+7)) reserStart=i+8
        reserEnd=i+9
    }

    private fun setScheduleBtn(i:Int){
        if(i-1<0||i+1>8){
            if(!scheduleBtn[i]){
                SelectedBtn(i)
                scheduleBtn[i] = true
            }
            else if(scheduleBtn[i]){
                notSelectedBtn(i)
                scheduleBtn[i] = false
            }
        }
        else if(scheduleBtn[i-1]&&scheduleBtn[i+1]){
            Toast.makeText(activity, "사이에 빈 시간이 없게 선택해주세요.", Toast.LENGTH_SHORT).show()
        }
        else if(!scheduleBtn[i] && checkEmptySchedule(i)){
            SelectedBtn(i)
            scheduleBtn[i] = true
        }
        else if(scheduleBtn[i] && checkEmptySchedule(i)){
            notSelectedBtn(i)
            scheduleBtn[i] = false
        }
    }

    private fun checkEmptySchedule(i:Int):Boolean{
        for(j:Int in 0..8){
            if(scheduleBtn[j]==true&&j<i){
                for(k:Int in j+1..i-1){
                    if(scheduleBtn[k]==false){
                        Toast.makeText(activity, "사이에 빈 시간이 없게 선택해주세요.", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
            else if(scheduleBtn[j]==true&&i<j){
                for(k:Int in i+1..j-1){
                    if(scheduleBtn[k]==false){
                        Toast.makeText(activity, "사이에 빈 시간이 없게 선택해주세요.", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun SelectedBtn(i:Int){
        val a="#37837C"
        if(i==0) binding.button.background.setTint(Color.parseColor(a))
        else if(i==1) binding.button2.background.setTint(Color.parseColor(a))
        else if(i==2) binding.button3.background.setTint(Color.parseColor(a))
        else if(i==3) binding.button4.background.setTint(Color.parseColor(a))
        else if(i==4) binding.button5.background.setTint(Color.parseColor(a))
        else if(i==5) binding.button6.background.setTint(Color.parseColor(a))
        else if(i==6) binding.button7.background.setTint(Color.parseColor(a))
        else if(i==7) binding.button8.background.setTint(Color.parseColor(a))
        else if(i==8) binding.button9.background.setTint(Color.parseColor(a))
    }
    private fun notSelectedBtn(i:Int){
        val a="#36DA90"
        if(i==0) binding.button.background.setTint(Color.parseColor(a))
        else if(i==1) binding.button2.background.setTint(Color.parseColor(a))
        else if(i==2) binding.button3.background.setTint(Color.parseColor(a))
        else if(i==3) binding.button4.background.setTint(Color.parseColor(a))
        else if(i==4) binding.button5.background.setTint(Color.parseColor(a))
        else if(i==5) binding.button6.background.setTint(Color.parseColor(a))
        else if(i==6) binding.button7.background.setTint(Color.parseColor(a))
        else if(i==7) binding.button8.background.setTint(Color.parseColor(a))
        else if(i==8) binding.button9.background.setTint(Color.parseColor(a))
    }
}