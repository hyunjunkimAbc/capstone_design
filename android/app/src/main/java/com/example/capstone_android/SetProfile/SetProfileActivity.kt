package com.example.capstone_android.SetProfile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.AddressActivity
import com.example.capstone_android.HomeActivity
import com.example.capstone_android.R
import com.example.capstone_android.Util.MainMenuId
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.databinding.ActivitySetprofileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_create.view.*
import kotlinx.android.synthetic.main.timedialog2.*
import java.text.SimpleDateFormat
import java.util.*



class SetProfileActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySetprofileBinding
    lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var storage = FirebaseStorage.getInstance()
    private lateinit var currentUserUid: String
    var checkchangebirth:Boolean=false
    private lateinit var date:String
    var photoUri: Uri?=null
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage
        date= SingleTonData.userInfo?.birthday.toString()
        val currentUser = auth.currentUser
        currentUserUid = currentUser?.uid!!
        val toolbar=binding.setuserprofiletoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title="내 프로필"
        binding.useremail.text=SingleTonData.userInfo?.email
        binding.setprofileusername.setText(SingleTonData.userInfo?.nickname)
        binding.setprofileuserbirth.text=SingleTonData.userInfo?.birthday
        binding.setprofileuseraddress.text=SingleTonData.userInfo?.address
        binding.setprofileuserexplain.setText(SingleTonData.userInfo?.profile_message)
        if(SingleTonData.userInfo?.photoUri!=null){
            val roundedCornersTransformation = RoundedCornersTransformation(20, 5)
            Glide.with(this).load(SingleTonData.userInfo?.photoUri).apply(RequestOptions().transform(
                CenterCrop(), CircleCrop()))
                .placeholder(R.drawable.loadingicon)
                .error(R.drawable.userprofile)
                .into(binding.setprofile)
        }
        else binding.setprofile.setImageResource(R.drawable.userprofile)
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
                                binding.setprofile.setImageBitmap(bitmap)
                            } else {
                                val source = ImageDecoder.createSource(this.contentResolver, photoUri!!)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                binding.setprofile.setImageBitmap(bitmap)
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

        binding.cardView12.setOnClickListener{
            val intent= Intent(this, AddressActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding.cardView11.setOnClickListener{
            showDateTimePickerDialog()
        }
        binding.setprofile.setOnClickListener{
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            filterActivityLauncher.launch(intent)
        }

    }
    private fun showDateTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val dateTimePickerDialog = Dialog(this)
        dateTimePickerDialog.setContentView(R.layout.timedialog2)
        val datePicker = dateTimePickerDialog.findViewById<DatePicker>(R.id.changeuserbirth)
        val okButton = dateTimePickerDialog.findViewById<Button>(R.id.timecomfirm)
        val cancelButton = dateTimePickerDialog.findViewById<Button>(R.id.timecancel)
        datePicker.init(currentYear, currentMonth, currentDay, null)


        okButton.setOnClickListener{
            val year= datePicker.year.toString()
            val month=if(datePicker.month<9)"0"+(datePicker.month+1).toString() else (datePicker.month+1).toString()
            val day=if(datePicker.dayOfMonth<10) "0"+datePicker.dayOfMonth.toString() else datePicker.dayOfMonth.toString()
            date="$year$month$day"
            checkchangebirth=true
            Log.d(TAG,date)
            binding.setprofileuserbirth.text=date
            dateTimePickerDialog.dismiss()
        }
        cancelButton.setOnClickListener{
            dateTimePickerDialog.dismiss()
        }

        dateTimePickerDialog.show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.changeprofile,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {

                finish()
                return true
            }
            R.id.toolbar_next_button->{
                if(photoUri!=null){
                    val userprofileimg="${currentUserUid}.jpg"
                    val storageRef=storage.reference.child("user_profile_image").child(userprofileimg)
                    storageRef.putFile(photoUri!!).addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val data=hashMapOf("nickname" to binding.setprofileusername.text.toString(),"profile_message" to binding.setprofileuserexplain.text.toString(),
                                "birthday" to date,"photoUri" to uri.toString())
                            db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).set(data, SetOptions.merge()).addOnSuccessListener{
                                SingleTonData.userInfo?.nickname=binding.setprofileusername.text.toString()
                                SingleTonData.userInfo?.birthday=date
                                SingleTonData.userInfo?.profile_message=binding.setprofileuserexplain.text.toString()
                                SingleTonData.userInfo?.photoUri= uri.toString()
                                db.collection("userAlarm").whereEqualTo("RealUserUid", "${Firebase.auth.uid}").get().addOnSuccessListener { quert->
                                    for (documentSnapshot in quert.documents) {
                                        val documentId = documentSnapshot.id
                                        val data = hashMapOf("username" to binding.setprofileusername.text.toString())
                                        db.collection("userAlarm").document(documentId).set(data,SetOptions.merge()).addOnSuccessListener {
                                        }
                                    }
                                    finish()
                                }

                                return@addOnSuccessListener
                            }
                        }
                    }
                }
                else{
                    val data=hashMapOf("nickname" to binding.setprofileusername.text.toString(),"profile_message" to binding.setprofileuserexplain.text.toString(),
                    "birthday" to date)
                     db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).set(data, SetOptions.merge()).addOnSuccessListener{
                        SingleTonData.userInfo?.nickname=binding.setprofileusername.text.toString()
                         SingleTonData.userInfo?.birthday=date
                         SingleTonData.userInfo?.profile_message=binding.setprofileuserexplain.text.toString()
                         db.collection("userAlarm").whereEqualTo("RealUserUid", "${Firebase.auth.uid}").get().addOnSuccessListener { quert->
                             for (documentSnapshot in quert.documents) {
                                 val documentId = documentSnapshot.id
                                 val data = hashMapOf("username" to binding.setprofileusername.text.toString())
                                 db.collection("userAlarm").document(documentId).set(data,SetOptions.merge()).addOnSuccessListener {
                                 }
                             }
                             finish()
                         }

                         return@addOnSuccessListener
                     }
                }



            }

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100) {
            binding.setprofileuseraddress.text=SingleTonData.userInfo?.address
        }

    }
    private fun showProfileImage() { // 프로필 사진 불러오기
        val user = auth.currentUser
        val storageReference = storage.reference
        var photoReference = storageReference.child("user_profile_image/${currentUserUid}.jpg")

        if (user != null && photoReference != null) {
            photoReference.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                if (it.isSuccessful) { // 사용자 설정 프로필 이미지
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.setprofile.setImageBitmap(bmp)
                } else { // 기본 설정 프로필 이미지
                    photoReference = storageReference.child("user_profile_image/default.jpg")

                    photoReference.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                        if(it.isSuccessful){
                            val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                            binding.setprofile.setImageBitmap(bmp)
                        } else{
                            println("Error getProfile image")
                        }
                    }

                }
            }.addOnFailureListener { exception ->
                println("Error show profile image: ${exception.message}")
            }
        }
    }
}