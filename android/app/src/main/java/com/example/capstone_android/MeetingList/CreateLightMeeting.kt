package com.example.capstone_android.MeetingList

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.R
import com.example.capstone_android.SearchAddress.SearchMap
import com.example.capstone_android.SelectHobbyActivity
import com.example.capstone_android.Util.LightTimeCheck
import com.example.capstone_android.Util.getImageResult
import com.example.capstone_android.data.lightData
import com.example.capstone_android.databinding.ActivityLightcreatemeetingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_create.view.*
import kotlinx.android.synthetic.main.fragment_sportfragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateLightMeeting:AppCompatActivity() {
    private lateinit var binding: ActivityLightcreatemeetingBinding
    private lateinit var TimeCheck : ArrayList<CheckBox>
     var StartTime :String?=null
     var EndTime:String?=null
    var hobbycheck :Boolean = false
    var addresscheck:Boolean=false
    var photoUri: Uri?=null
    var hobby:String ?=null
    private lateinit var addressname:String
    private lateinit var address:String
    var disx:Double?=null
    var disy:Double?=null
    var date:String?=null
    var date2:String?=null
    lateinit var storage: FirebaseStorage
    lateinit var db : FirebaseFirestore
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityLightcreatemeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lightprogressBar.visibility= View.GONE
        db= Firebase.firestore
        storage = Firebase.storage
        TimeCheck= arrayListOf(binding.time05,binding.time053,binding.time06,binding.time063,binding.time07,
            binding.time073,binding.time08,binding.time083,binding.time09,binding.time093,binding.time10,binding.time103
            ,binding.time11,binding.time113,binding.time12,binding.time123,binding.time13,binding.time133,binding.time14,
            binding.time143,binding.time15,binding.time153,binding.time16,binding.time163,binding.time17,binding.time173)

        val datePicker=binding.lightmeetingdate
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        datePicker.init(year,month,dayOfMonth,object:DatePicker.OnDateChangedListener{
            override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                val pmonth=if(monthOfYear<9)"0"+(monthOfYear+1).toString() else (monthOfYear+1).toString()
                val pdayOfMonth=if(dayOfMonth<10) "0"+dayOfMonth.toString() else dayOfMonth.toString()
                date="$year$pmonth$pdayOfMonth"
                date2="${year}년${pmonth}월${pdayOfMonth}일"
                Log.d(TAG,date!!)
            }

        })
        val timetest="07:30"
        var after_string = timetest.split(":")
        Log.d(TAG,after_string[0]+after_string[1])
        for((index,checkbox) in TimeCheck.withIndex()){
            checkbox.setOnClickListener{
                Log.d(TAG,"TIMECHECK=${LightTimeCheck.timecheck}")

                if(LightTimeCheck.timecheck==0){
                    TimeCheck[index].isChecked=true
                    LightTimeCheck.index=index
                    LightTimeCheck.timecheck=1
                    StartTime= TimeCheck[index].text.toString()
                    EndTime=TimeCheck[index].contentDescription.toString()
                    Log.d(TAG,"시작시간=${StartTime} 종료시간=${EndTime}")
                }
                else if(LightTimeCheck.timecheck==1){
                    val idx=LightTimeCheck.index
                    if(index>idx){
                        StartTime= TimeCheck[idx].text.toString()
                        EndTime=TimeCheck[index].contentDescription.toString()
                        for(i in idx..index){
                            TimeCheck[i].isChecked=true
                        }
                    }
                    else{
                        StartTime= TimeCheck[index].text.toString()
                        EndTime=TimeCheck[idx].contentDescription.toString()
                        for(i in index..idx){
                            TimeCheck[i].isChecked=true
                        }
                    }
                    LightTimeCheck.timecheck=2
                }
                else{
                    Log.d(TAG,"시작시간=${StartTime} 종료시간=${EndTime}")
                    LightTimeCheck.timecheck=0
                    LightTimeCheck.index=0
                    for(data in TimeCheck) data.isChecked=false
                }
            }
        }
        val filterActivityLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode == AppCompatActivity.RESULT_OK && it.data !=null) {
                    photoUri = it.data?.data
                    try {
                        photoUri?.let {
                            if(Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    photoUri
                                )
                                binding.lightimg.clubprofile.setImageBitmap(bitmap)
                            } else {
                                val source = ImageDecoder.createSource(this.contentResolver, photoUri!!)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                binding.lightimg.setImageBitmap(bitmap)
                            }
                        }


                    }catch(e:Exception) {
                        e.printStackTrace()
                    }
                } else if(it.resultCode == AppCompatActivity.RESULT_CANCELED){
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }else{
                    Log.d("ActivityResult","something wrong")
                }
            }


        val createlighttoolbar=binding.createlighttoolbar
        setSupportActionBar(createlighttoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title="번개모임 생성"

        binding.lightmeetinghobby.setOnClickListener{
            val intent= Intent(this, SelectHobbyActivity::class.java)
            intent.putExtra("key","select")
            startActivityForResult(intent,5)
        }
        binding.lightaddress.setOnClickListener{
            val intent = Intent(this, SearchMap::class.java)
            intent.putExtra("create","hello")
            startActivityForResult(intent,9)
        }


        binding.CreateLightButton.setOnClickListener{
            if(photoUri==null){
                Toast.makeText(this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
            }
            else if(!hobbycheck){
                Toast.makeText(this, "관심사를 선택해주세요", Toast.LENGTH_LONG).show();
            }
            else if(binding.lightmeetingname.text.toString()==""){
                Toast.makeText(this, "번개모임 제목을 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(!addresscheck){
                Toast.makeText(this, "모임하실 장소를 정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(StartTime==null||EndTime==null){
                Toast.makeText(this, "모임 시간을 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(binding.lightexplain.text.toString()==""){
                Toast.makeText(this, "모임에 대한 조건과 설명을 적어주세요", Toast.LENGTH_LONG).show();
            }
            else if(binding.lightnumber.text.toString()==""||binding.lightnumber.text.toString().toInt()<=0){
                Toast.makeText(this, "모집 인원을 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(date==null){
                Toast.makeText(this, "날짜를 지정해주세요", Toast.LENGTH_LONG).show();
            }
            else uploadLightContent()
        }
        binding.lightimg.setOnClickListener{
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            filterActivityLauncher.launch(intent)
        }
    }
    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==5&&resultCode== RESULT_OK){
            hobby= data?.extras?.getString("hobby").toString()
            binding.lightmeetinghobby.setImageResource(getImageResult(hobby!!))
            hobbycheck=true
        }
        else if(requestCode==9&&resultCode== RESULT_OK){
            addresscheck=true
            addressname= data?.extras?.getString("name").toString()
            address= data?.extras?.getString("address").toString()
            disx = data?.extras?.getDouble("disx")
            disy = data?.extras?.getDouble("disy")
            binding.lightaddresstext.apply{
                text=addressname
                setTextColor(R.color.black)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("SimpleDateFormat")
    fun uploadLightContent(){
        RoadData()
        val s1:String= Firebase.auth.currentUser?.uid.toString()
        val s2:String= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val makeuid=s1.plus(s2)

        val clubprofileimagename=makeuid.plus(".jpg")
        val storageRef=storage.reference.child("lighting_meeting_room").child(clubprofileimagename)
        storageRef.putFile(photoUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val lightdata = lightData()
                val starttimediv=StartTime?.split(":")
                val endtimediv=EndTime?.split(":")
                lightdata.title= binding.lightmeetingname.text.toString()
                lightdata.max=binding.lightnumber.text.toString()
                lightdata.info_text=binding.lightexplain.text.toString()
                lightdata.writer_uid=Firebase.auth.currentUser?.uid.toString()
                lightdata.address=address
                lightdata.addressname=addressname
                lightdata.category=hobby
                lightdata.upload_time=System.currentTimeMillis()
                lightdata.start_time=date2?.plus(starttimediv?.get(0)).plus("시").plus(starttimediv?.get(1)).plus("분")
                lightdata.end_time=date2?.plus(endtimediv?.get(0)).plus("시").plus(endtimediv?.get(1)).plus("분")
                lightdata.start_time2=StartTime
                lightdata.end_time2=EndTime
                lightdata.date=date
                lightdata.imageUrl=uri.toString()
                lightdata.positionx=disx
                lightdata.positiony=disy
                lightdata.uid=makeuid
                db.collection("lighting_meeting_room").document(makeuid).set(lightdata).addOnSuccessListener{
                    db.collection("lighting_meeting_room").document(makeuid).update("member_list",
                        FieldValue.arrayUnion(Firebase.auth.currentUser?.uid.toString())).addOnSuccessListener{
                        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).update("lightmeeting_room_id_list",FieldValue.arrayUnion(makeuid)).addOnSuccessListener{
                            ClearData()
                            setResult(Activity.RESULT_OK)
                            finish()
                            Log.d(TAG, lightdata.toString())
                        }
                    }
                }
            }
        }
    }
    private fun RoadData(){
        binding.lightprogressBar.visibility= View.VISIBLE
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){
        binding.lightprogressBar.visibility=View.GONE
        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    override fun onDestroy() {
        LightTimeCheck.timecheck=0
        LightTimeCheck.index=0
        super.onDestroy()
    }
}
