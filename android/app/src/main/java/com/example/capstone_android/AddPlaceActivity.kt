package com.example.capstone_android

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.SearchAddress.SearchMap
import com.example.capstone_android.data.PlaceRentalRoom
import com.example.capstone_android.databinding.ActivityAddplaceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddPlaceActivity : AppCompatActivity() {
    var photoUri:Uri?=null

    private val binding by lazy {
        ActivityAddplaceBinding.inflate(layoutInflater)
    }
    val db = Firebase.firestore
    val st = FirebaseStorage.getInstance()
    lateinit var auth: FirebaseAuth

    private val OPEN_GALLERY = 1
    var image: Uri? = null

    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "place_rental_room/"
    }

    var category: String? = null
    var placeName: String? = null
    var address: String? = null
    var detailaddress: String? = null
    var positionx: Double? = null
    var positiony: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth




        // 툴바 설정
        setSupportActionBar(binding.addPlaceToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("장소등록")

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
                                binding.placeImageBtn.setImageBitmap(bitmap)
                            } else {
                                val source = ImageDecoder.createSource(this.contentResolver, photoUri!!)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                binding.placeImageBtn.setImageBitmap(bitmap)
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

        binding.placeImageBtn.setOnClickListener() { // 첨부파일 이미지 버튼 클릭
            //uploadDialog()
            // 갤러리로 이동하여 이미지 파일을 이미지뷰에 가져옴
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            filterActivityLauncher.launch(intent)
        }
        binding.address.setOnClickListener() {
             val intent = Intent(this, SearchMap::class.java)
            intent.putExtra("create","hello")
            startActivityForResult(intent,9)
        }

        binding.addPlaceBtn.setOnClickListener() {
            if (binding.placeName.getText().toString() == "") {
                Toast.makeText(
                    this,
                    "장소 이름을 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (photoUri == null) {
                Toast.makeText(this, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }/*
            else if (interest_array.isNullOrEmpty()){
                println("체크된 관심사가 없음")
                Toast.makeText(
                    this,
                    "관심사를 선택해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (address==null){
                Toast.makeText(
                    this,
                    "주소를 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
            else if (binding.numOfPeopleEditText2.getText().toString() == "") {
                Toast.makeText(
                    this,
                    "최대수용인원을 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.InfoText.getText().toString() == "") {
                Toast.makeText(
                    this,
                    "장소 정보를 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                var placeData = PlaceRentalRoom()
                var placeDocument = db.collection("place_rental_room").document()
                val storageRef=st.reference.child("lighting_meeting_room").child(placeDocument.id + ".jpg")
                storageRef.putFile(photoUri!!).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener{ uri->
                        placeData.Uid = placeDocument.id
                        placeData.category = category ?: "운동"
                        placeData.title = binding.placeName.getText().toString()
                        placeData.info_text = binding.InfoText.getText().toString()
                        placeData.writer_uid = Firebase.auth.currentUser?.uid.toString()
                        placeData.address = address ?: "서울 서대문구"
                        placeData.addressdetail = detailaddress ?: "서울 서대문구 가좌로 85"
                        placeData.max = binding.numOfPeopleEditText2.getText().toString()
                        placeData.upload_time = System.currentTimeMillis()
                        placeData.positionx = positionx ?: 126.927547
                        placeData.positiony = positiony ?: 37.5805720
                        placeData.imageUrl=uri.toString()
                        placeDocument.set(placeData).addOnSuccessListener {
                            db.collection("user").document(auth.currentUser?.uid.toString())
                                .update("place_id_list", FieldValue.arrayUnion(placeDocument.id))
                            // Firebase Storage로 이미지 전송
                            Toast.makeText(this, "장소를 등록하였습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    // 갤러리에서 사진을 선택하면 실행되는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode ==9&&resultCode== RESULT_OK) {
            address= data?.extras?.getString("address")
            detailaddress= data?.extras?.getString("detailad")
            placeName = data?.extras?.getString("name")
            positionx = data?.extras?.getDouble("disx")
            positiony = data?.extras?.getDouble("disy")
            binding.useraddress.text=detailaddress
                }
            }


}