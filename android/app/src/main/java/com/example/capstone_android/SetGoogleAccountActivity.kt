package com.example.capstone_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivitySetgoogleaccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.lang.Thread.sleep

// 구글계정 개인정보 설정
class SetGoogleAccountActivity : AppCompatActivity()  {
    private val binding by lazy {
        ActivitySetgoogleaccountBinding.inflate(layoutInflater)
    }

    val db = Firebase.firestore
    val st = FirebaseStorage.getInstance()
    private val OPEN_GALLERY = 1
    lateinit var storage : FirebaseStorage
    var image : Uri? = null
    var imgName : String? = null
    var selected_profile_img : Int = 0

    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "upload_images/"
    }
    var selected_interest = ""

    // 선택된 관심사를 담는 배열 (DB 필드 이름: interest_array)
    var interest_array = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 상단 툴바
        setSupportActionBar(binding.initToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        supportActionBar!!.setTitle("개인정보 설정") // 툴바 제목 설정


        binding.profileImageButton.setOnClickListener(){ // 첨부파일 이미지 버튼 클릭
            println("이미지 버튼 클릭")
            //uploadDialog()
            // 갤러리로 이동하여 이미지 파일을 이미지뷰에 가져옴
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            startActivityForResult(intent, OPEN_GALLERY)
        }

        // 이메일 중복 검사를 하기위함
        val user_data = db.collection("user")

        // 생년월일 선택 DatePicker
        val datePicker : DatePicker = binding.dpSpinner


        // 가입하기 버튼 클릭
        binding.InitButton.setOnClickListener(){
            println("저장하기 버튼 클릭")
            select_interest_CheckBox()
            if(binding.NicknameEditText.getText().toString()==""){    // nickname을 입력하지 않았을 때
                println("닉네임을 입력하지않음")
                Toast.makeText(
                    this,
                    "닉네임을 입력하지않았습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (binding.NicknameEditText.length()>15){     // 닉네임의 글자수가 15자 초과하면
                println("닉네임 글자수 15자 초과")
                Toast.makeText(
                    this,
                    "닉네임 글자수 15자를 초과하였습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (interest_array.isNullOrEmpty()){
                println("체크된 관심사가 없음")
                Toast.makeText(
                    this,
                    "관심사를 선택해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                println("구글계정 개인정보 설정완료")

                val user = FirebaseAuth.getInstance().currentUser

                val col = db.collection("user").document("${user?.uid}")
                val itemMap = hashMapOf(
                    "email" to user?.email,
                    "nickname" to binding.NicknameEditText.getText().toString(),
                    "uid" to user?.uid,
                    "edit_time" to System.currentTimeMillis(),
                    "profile_message" to "",
                    "birthday" to datePicker.year.toString()+datePicker.month.toString()+datePicker.dayOfMonth.toString(),
                    "interest_array" to interest_array
                )
                col.set(itemMap)

                // 프로필 이미지가 선택되었으면
                if (selected_profile_img==1){
                    // Firebase Storage로 이미지 전송
                    imgName = "${user?.uid}"
                    var storageReference =
                        st?.reference?.child("user_profile_image")?.child(imgName!!)

                    storageReference?.putFile(image!!)?.addOnSuccessListener {
                        println("이미지 업로드 성공")
                    }
                }
                // 스낵바(토스트) 메시지
                Toast.makeText(
                    this,
                    "개인정보 설정을 완료하였습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                sleep(3000)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }

        }
    }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                // toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // 갤러리에서 사진을 선택하면 실행되는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == OPEN_GALLERY){
                image = data?.data
                try{

                    var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,image)
                    binding.profileImageButton.setImageBitmap(bitmap)
                    selected_profile_img = 1
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun select_interest_CheckBox()/*:Array<String> */{
        if(binding.sportsCheckBox.isChecked) {
            interest_array.add("운동")
        }
        if(binding.tripCheckBox.isChecked) {
            interest_array.add("여행")
        }
        if(binding.musicCheckBox.isChecked) {
            interest_array.add("음악")
        }
        if(binding.societyCheckBox.isChecked) {
            interest_array.add("사교/직업")
        }
        if(binding.readCheckBox.isChecked) {
            interest_array.add("독서")
        }
        if(binding.cookCheckBox.isChecked) {
            interest_array.add("요리")
        }
        if(binding.photoCheckBox.isChecked) {
            interest_array.add("사진")
        }
        if(binding.gameCheckBox.isChecked) {
            interest_array.add("게임")
        }
        if(binding.danceCheckBox.isChecked) {
            interest_array.add("댄스")
        }
        if(binding.carCheckBox.isChecked) {
            interest_array.add("차/오토바이")
        }
        if(binding.artCheckBox.isChecked) {
            interest_array.add("공예")
        }
        if(binding.volunteerCheckBox.isChecked) {
            interest_array.add("봉사활동")
        }
        if(binding.studyCheckBox.isChecked) {
            interest_array.add("공부/자기개발")
        }
        println("선택한 관심사 카테고리"+ interest_array)
    }
}