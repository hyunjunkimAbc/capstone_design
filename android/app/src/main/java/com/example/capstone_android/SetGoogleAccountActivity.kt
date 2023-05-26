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
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.databinding.ActivitySetGoogleAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.lang.Thread.sleep

// 구글계정 개인정보 설정
class SetGoogleAccountActivity : AppCompatActivity()  {
    private val binding by lazy {
        ActivitySetGoogleAccountBinding.inflate(layoutInflater)
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
        const val UPLOAD_FOLDER = "user_profile_image"
    }

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

        // 생년월일 선택 DatePicker
        val datePicker : DatePicker = binding.dpSpinner

        binding.setInterestBtn.setOnClickListener(){
            val intent = Intent(this, SelectHobbyActivity::class.java)
            intent.putExtra("key", "selecthobby")
            startActivity(intent)
        }

        // 가입하기 버튼 클릭
        binding.InitButton.setOnClickListener(){
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
//            else if (SingleTonData.userInfo?.interest_array==null){
//                Toast.makeText(
//                    this,
//                    "관심사를 선택해주세요.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
            else {
                var user = FirebaseAuth.getInstance().currentUser
                var signdata= SignUpData()
                signdata.email=Firebase.auth.currentUser?.email
                signdata.nickname= binding.NicknameEditText.getText().toString()
                signdata.uid=Firebase.auth.currentUser?.uid
                signdata.birthday=datePicker.year.toString()+datePicker.month.toString()+datePicker.dayOfMonth.toString()
                signdata.timestamp=System.currentTimeMillis()
                signdata.profile_message=""
//                signdata.interest_array=SingleTonData.userInfo?.interest_array!!
                signdata.edit_time=System.currentTimeMillis()
                db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).set(signdata)

                // 프로필 이미지가 선택되었으면
                if (selected_profile_img==1){
                    // Firebase Storage로 이미지 전송
                    imgName = "${user?.uid ?: String}"
                    var storageReference =
                        st?.reference?.child(UPLOAD_FOLDER)?.child(imgName!!)

                    storageReference?.putFile(image!!)?.addOnSuccessListener {
                        println("이미지 업로드 성공")
                    }
                }
                else {
                    // 프로필 이미지 설정X
                }
                // 스낵바(토스트) 메시지
                Toast.makeText(
                    this,
                    "개인정보 설정을 완료하였습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, ConciergeActivity::class.java)
                startActivity(intent)
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


}