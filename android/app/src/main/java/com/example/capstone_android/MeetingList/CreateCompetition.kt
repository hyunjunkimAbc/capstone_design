package com.example.capstone_android.MeetingList

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
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
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.R
import com.example.capstone_android.SearchAddress.SearchMap
import com.example.capstone_android.SelectHobbyActivity
import com.example.capstone_android.Util.getImageResult
import com.example.capstone_android.data.CompetitionData
import com.example.capstone_android.databinding.ActivityCompetitioncreateBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_create.view.*
import java.text.SimpleDateFormat
import java.util.*


class CreateCompetition: AppCompatActivity() {
    private lateinit var binding: ActivityCompetitioncreateBinding
    private lateinit var addressname:String
    private lateinit var address:String
    var starttime:String?=null
    var endtime:String?=null
    var disx:Double?=null
    var disy:Double?=null
    var detailad:String?=null
    var hobbycheck :Boolean = false
    var addresscheck:Boolean=false
    var photoUri: Uri?=null
    var hobby:String ?=null
    lateinit var storage: FirebaseStorage
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db= Firebase.firestore
        storage = Firebase.storage
        binding = ActivityCompetitioncreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.competitionpgb.visibility=View.GONE
        binding.competitionstartbox.setOnClickListener{
            showDateTimePickerDialog("start")
        }
        binding.competitionendbox.setOnClickListener{
            showDateTimePickerDialog("end")
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
                                binding.createcompetitiontimg.clubprofile.setImageBitmap(bitmap)
                            } else {
                                val source = ImageDecoder.createSource(this.contentResolver, photoUri!!)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                binding.createcompetitiontimg.setImageBitmap(bitmap)
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

        val competitiontoolbar=binding.competitiontoolbar
        setSupportActionBar(competitiontoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title="대회 생성"

        binding.createcompetitionhobby.setOnClickListener{
            val intent= Intent(this, SelectHobbyActivity::class.java)
            intent.putExtra("key","select")
            startActivityForResult(intent,5)
        }
        binding.createcompetitionaddress.setOnClickListener{
            val intent = Intent(this, SearchMap::class.java)
            intent.putExtra("create","hello")
            startActivityForResult(intent,9)
        }
        binding.createcompetitiontimg.setOnClickListener{
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            filterActivityLauncher.launch(intent)
        }
        binding.CreateLightButton.setOnClickListener{
            if(photoUri==null){
                Toast.makeText(this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
            }
            else if(!hobbycheck){
                Toast.makeText(this, "관심사를 선택해주세요", Toast.LENGTH_LONG).show();
            }
            else if(binding.createcompetitiontitle.text.toString()==""){
                Toast.makeText(this, "대회 제목을 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(!addresscheck){
                Toast.makeText(this, "대회 장소를 정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(starttime==null||endtime==null){
                Toast.makeText(this, "대회 시간을 설정해주세요", Toast.LENGTH_LONG).show();
            }else if(binding.competitionexplain.text.toString()==""){
                Toast.makeText(this, "대회에 대한 조건과 설명을 적어주세요", Toast.LENGTH_LONG).show();
            }else if(binding.competitionnumber.text.toString()==""||binding.competitionnumber.text.toString().toInt()<=0){
                Toast.makeText(this, "모집 인원을 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else uploadLightContent()
        }

    }

    private fun showDateTimePickerDialog(key:String) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val dateTimePickerDialog = Dialog(this)
        dateTimePickerDialog.setContentView(R.layout.timedialog)
        val datePicker = dateTimePickerDialog.findViewById<DatePicker>(R.id.competitiondatepicker)
        val timePicker = dateTimePickerDialog.findViewById<TimePicker>(R.id.copetitiontimepicker)
        val okButton = dateTimePickerDialog.findViewById<Button>(R.id.competitiontimeok)
        datePicker.init(currentYear, currentMonth, currentDay, null)
        timePicker.setIs24HourView(true)
        timePicker.hour = currentHour
        timePicker.minute = currentMinute

        okButton.setOnClickListener{
            val year= datePicker.year.toString()
            val month=if(datePicker.month<9)"0"+(datePicker.month+1).toString() else (datePicker.month+1).toString()
            val day=if(datePicker.dayOfMonth<10) "0"+datePicker.dayOfMonth.toString() else datePicker.dayOfMonth.toString()
            val hour=if(timePicker.hour<10)"0"+(timePicker.hour).toString()else (timePicker.hour).toString()
            val min=if(timePicker.minute<10)"0"+(timePicker.minute).toString() else timePicker.minute.toString()
            val time=year.plus("년").plus(month+"월").plus(day+"일")
            val time2=hour.plus("시").plus(min+"분")

            if(key=="start") {
                starttime=time.plus(time2)
                binding.competitionstarttime.text = time
                binding.competitionstarttimehhmm.text = time2
            }
            else if(key=="end"){
                endtime=time.plus(time2)
                binding.competitionendtime.text=time
                binding.competitionendtimehhmm.text=time2
            }
            dateTimePickerDialog.dismiss()
        }


        dateTimePickerDialog.show()
    }
    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==5&&resultCode== RESULT_OK){
            hobby= data?.extras?.getString("hobby").toString()
            binding.createcompetitionhobby.setImageResource(getImageResult(hobby!!))
            hobbycheck=true
        }
        else if(requestCode==9&&resultCode== RESULT_OK){
            addresscheck=true
            detailad=data?.extras?.getString("detailad").toString()
            addressname= data?.extras?.getString("name").toString()
            address= data?.extras?.getString("address").toString()
            disx = data?.extras?.getDouble("disx")
            disy = data?.extras?.getDouble("disy")
            binding.createcompetitionaddresstext.apply{
                text=addressname
                setTextColor(R.color.black)
            }
        }
    }
    fun uploadLightContent(){
        RoadData()
        val s1:String= Firebase.auth.currentUser?.uid.toString()
        val s2:String= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val makeuid=s1.plus(s2)
        val competitionprofileimagename=makeuid.plus(".jpg")
        val storageRef=storage.reference.child("competition_room").child(competitionprofileimagename)
        storageRef.putFile(photoUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val competitiondata=CompetitionData()
                competitiondata.title=binding.createcompetitiontitle.text.toString()
                competitiondata.max=binding.competitionnumber.text.toString()
                competitiondata.info_text=binding.competitionexplain.text.toString()
                competitiondata.writer_uid=Firebase.auth.currentUser?.uid.toString()
                competitiondata.address=address
                competitiondata.category=hobby
                competitiondata.upload_time=System.currentTimeMillis()
                competitiondata.start_time=starttime
                competitiondata.end_time=endtime
                competitiondata.num_of_positive=0
                competitiondata.location=detailad.plus(" ").plus(addressname)
                competitiondata.positionx=disx
                competitiondata.positiony=disy
                competitiondata.Uid=makeuid
                competitiondata.imageUrl=uri.toString()
                db.collection("competition_room").document(makeuid).set(competitiondata).addOnSuccessListener{
                    db.collection("competition_room").document(makeuid).update("member_list",
                        FieldValue.arrayUnion(Firebase.auth.currentUser?.uid.toString())).addOnSuccessListener{
                        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).update("competition_create_id_list",
                            FieldValue.arrayUnion(makeuid)).addOnSuccessListener{
                            ClearData()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                }
            }
        }
    }
    private fun RoadData(){
        binding.competitionpgb.visibility= View.VISIBLE
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){
        binding.competitionpgb.visibility= View.GONE
        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
}