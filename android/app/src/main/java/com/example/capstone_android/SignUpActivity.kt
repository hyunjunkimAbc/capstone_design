package com.example.capstone_android

import android.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class SignUpActivity : AppCompatActivity() { // 회원가입 화면
    private val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //setContentView(R.layout.activity_signup)

        // 상단 툴바
//        val toolbar = binding.signupToolbar
        setSupportActionBar(binding.signupToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        supportActionBar!!.setTitle("회원가입") // 툴바 제목 설정


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

        // 회원가입 중 관심사 선택 항목들
        val interest_items = listOf("운동", "여행", "음악", "사교/직업", "독서", "요리", "사진", "게임", "댄스",
            "차/오토바이", "반려동물", "공예", "봉사활동", "공부/자기개발")
//        val interest_items = resources.getStringArray(R.array.interest_items)
        val myAdapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, interest_items)

        val spinner = binding.spinner
        spinner.adapter = myAdapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 관심사를 선택하면 position값으로 구별하여 selected_interest 변수의 값을 세팅
                when(position) {
                    0   ->  {
                        println("운동 선택")
                        selected_interest = "운동"
                    }
                    1   ->  {
                        println("여행 선택")
                        selected_interest = "여행"
                    }
                    2   ->  {
                        println("음악 선택")
                        selected_interest = "음악"
                    }
                    3   ->  {
                        println("사교/직업 선택")
                        selected_interest = "사교/직업"
                    }
                    4   ->  {
                        println("독서 선택")
                        selected_interest = "독서"
                    }
                    5   ->  {
                        println("요리 선택")
                        selected_interest = "요리"
                    }
                    6   ->  {
                        println("사진 선택")
                        selected_interest = "사진"
                    }
                    7   ->  {
                        println("게임 선택")
                        selected_interest = "게임"
                    }
                    8   ->  {
                        println("댄스 선택")
                        selected_interest = "댄스"
                    }
                    9   ->  {
                        println("차/오토바이 선택")
                        selected_interest = "차/오토바이"
                    }
                    10   ->  {
                        println("반려동물 선택")
                        selected_interest = "반려동물"
                    }
                    11   ->  {
                        println("공예 선택")
                        selected_interest = "공예"
                    }
                    12   ->  {
                        println("봉사활동 선택")
                        selected_interest = "봉사활동"
                    }
                    13   ->  {
                        println("공부/자기개발 선택")
                        selected_interest = "공부/자기개발"
                    }
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.interestButton.setOnClickListener(){
            val intent = Intent(this, SelectInterestActivity::class.java)
            startActivity(intent)
        }

        // 가입하기 버튼 클릭
        binding.SignUpButton.setOnClickListener(){
            println("가입하기 버튼 클릭")
            if (binding.EmailEditText.getText().toString()==""){     // email을 입력하지 않았을 때
                println("이메일을 입력하지 않음")
                Toast.makeText(
                    this,
                    "이메일을 입력하지않았습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (binding.PWEditText.getText().toString()==""){     // 비밀번호를 입력하지 않았을 때
                println("비밀번호를 입력하지않음")
                Toast.makeText(
                    this,
                    "비밀번호를 입력하지않았습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (binding.PWEditText.getText().toString()!=binding.PWEditText2.getText().toString()){     // 첫번째 적은 비밀번호와 두번째 적은 비밀번호가 같지 않을때
                println("비밀번호가 같지않음")
                Toast.makeText(
                    this,
                    "비밀번호가 같지않습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if(binding.NicknameEditText.getText().toString()==""){    // nickname을 입력하지 않았을 때
                println("닉네임을 입력하지않음")
                Toast.makeText(
                    this,
                    "닉네임을 입력하지않았습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (binding.PWEditText.length()<6){     // 비밀번호 글자수가 6자 미만이면
                println("비밀번호 글자수 6자 미만")
                Toast.makeText(
                    this,
                    "비밀번호를 6자 이상 입력해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (binding.PWEditText.length()>15){     // 비밀번호 글자수가 15자 초과하면
                println("비밀번호 글자수 15자 초과")
                Toast.makeText(
                    this,
                    "비밀번호를 15자 이하로 입력해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (binding.NicknameEditText.length()>15){     // 닉네임의 글자수가 15자 초과하면
                println("닉네임 글자수 15자 초과")
                Toast.makeText(
                    this,
                    "닉네임 글자수 15자를 초과하였습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else {
                Firebase.auth.createUserWithEmailAndPassword(
                    binding.EmailEditText.getText().toString(),
                    binding.PWEditText.getText().toString()
                )
                    //findViewById<EditText>(R.id.EmailEditText).getText().toString(),
                    //findViewById<EditText>(R.id.PWEditText).getText().toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            println("회원가입 성공")
                            // DB postings 컬렉션 레퍼런스 가져오기
                            val user = FirebaseAuth.getInstance().currentUser
                            val col = db.collection("user")
                            val itemMap = hashMapOf(
                                "email" to binding.EmailEditText.getText().toString(),
                                "password" to binding.PWEditText.getText().toString(),
                                "nickname" to binding.NicknameEditText.getText().toString(),
                                "uid" to (user?.uid ?: String),
                                "birthday" to datePicker.year.toString()+datePicker.month.toString()+datePicker.dayOfMonth.toString(),
                                "interest" to selected_interest,
                                "edit_time" to System.currentTimeMillis(),
                                "profile_message" to ""
                            )
                            col.add(itemMap)

                            // 프로필 이미지가 선택되었으면
                            if (selected_profile_img==1) {
                                // Firebase Storage로 이미지 전송
                                imgName = "${user?.uid ?: String}"
                                var storageReference =
                                    st?.reference?.child("user_profile_image")?.child(imgName!!)

                                storageReference?.putFile(image!!)?.addOnSuccessListener {
                                    println("이미지 업로드 성공")
                                }
                            }
/*
                            //추가: friendCommit에 friendArr 과 nickName
                            val col_friendCommit = db.collection("friendCommit")
                            val itemMap_friendCommit = hashMapOf(
                                "nickName" to binding.NicknameEditText.getText().toString(),
                                "friendArr" to arrayListOf<String>()
                            )
                            col_friendCommit.add(itemMap_friendCommit)
*/
                            // 스낵바(토스트) 메시지
                            Toast.makeText(
                                this,
                                "회원가입에 성공하였습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                        // 이메일 중복 검사
                        else if (binding.EmailEditText.getText().toString()!=""){     // email을 입력했을때
                            user_data.get().addOnSuccessListener {// 이메일 중복 검사를 위해 데이터베이스 읽기
                                for(d in it){
                                    if(d["email"]==binding.EmailEditText.getText().toString()){
                                        println("해당 이메일이 이미 존재")
                                        Toast.makeText(
                                            this,
                                            "해당 이메일이 이미 존재합니다.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                        else {
                            println("회원가입 실패 ${it.exception?.message}")
                            Toast.makeText(
                                this,
                                "이메일 형식이 아닙니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

/*    //액션버튼 메뉴 액션바에 집어 넣기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }*/
    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                // toolbar의 back키 눌렀을 때 동작
                // 액티비티 뒤로 이동
                finish()
                return true
            }
/*            R.id.action_search -> {
                // 검색 버튼 눌렀을 때
                Toast.makeText(applicationContext, "검색 이벤트 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
            R.id.action_share -> {
                // 공유 버튼 눌렀을 때
                Toast.makeText(applicationContext, "공유 이벤트 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }*/
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