package com.example.capstone_android
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserUid: String
    private var storage = FirebaseStorage.getInstance()
    private var imageUri: Uri? = null

    /*private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            // 파이어베이스에 사진 업로드
            val user = auth.currentUser
            if (user != null) {
                val storageRef = storage.reference.child("users/${user.uid}/profile.jpg")
                storageRef.putFile(imageUri!!)
                    .addOnSuccessListener {
                        // 유저 프로필 이미지 URL 업데이트
                        storageRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                val profileUpdates = userProfileChangeRequest {
                                    photoUri = uri
                                }
                                user.updateProfile(profileUpdates)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // 새 프로필 이미지
                                            showProfileImage()
                                        } else {
                                            println("Error updating user profile: ${task.exception?.message}")
                                        }
                                    }
                            }
                            .addOnFailureListener { exception ->
                                println("Error getting download URL: ${exception.message}")
                            }
                    }
                    .addOnFailureListener { exception ->
                        println("Error uploading profile image: ${exception.message}")
                    }
            }
        }
    }*/

    val uid : String? = null
    val db = Firebase.firestore

    private val email : String = "user01@gmail.com"
    private val pw : String = "user01_1234"

    lateinit var userImage : ImageView

    lateinit var nickName : TextView
    lateinit var birth : TextView
    lateinit var userLocation : TextView
    lateinit var userMessage : TextView

    lateinit var button_Notification : ImageButton
    lateinit var button_ModifyProfile : Button
    lateinit var button_ModifyMyMeetingRoom : Button
    lateinit var button_Logout : Button
    lateinit var button_LogIn : Button
    lateinit var button_JoinMebership : ImageButton

    companion object {
        const val REQUEST_IMAGE = 100
    }

    data class UserProfile( // 프로필 정보
        val name: String = "", // 이름
        val birth: String = "", // 생년월일
        val location: String = "", // 위치정보
        val comment: String = "" // 자기소개글
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        storage = Firebase.storage

        //테스트용 실제로는 이  activity에 진입할 때는 이미 로그인이 되어 있을 것임
        signIn()

        val currentUser = auth.currentUser

        if (currentUser != null) { // 로그인 한 경우
            println("Sign in")
            currentUserUid = currentUser.uid
            initData_Signed()
            showProfile()

            // 정보 수정 버튼 설정
            button_ModifyProfile.setOnClickListener {
                // "수정"이면 프로필 수정 가능하게 변경
                if (button_ModifyProfile.tag == "수정") {

                    button_ModifyProfile.tag = "저장"
                    editProfileFields(true)
                } else {
                    // "저장"이면 프로필 정보 업데이트해서 반영
                    updateProfileInfo()
                }
            }

        } else { // 로그인 하지 않은 경우
            initData_NotSigned()
            println("NOT Sign in")
        }

    }

    // 로그인 한 경우 초기화 함수
    fun initData_Signed(){ // 로그인 한 경우 초기화 함수
        setContentView(R.layout.profile_signed_in)

        userImage = findViewById(R.id.userImage1)
        nickName = findViewById(R.id.nickname)
        birth = findViewById(R.id.birth)
        userLocation = findViewById(R.id.mylocation)
        userMessage = findViewById(R.id.introduce_comment)

        button_Notification = findViewById(R.id.button_notification)
        button_ModifyProfile = findViewById(R.id.button_modifyProfile)
        button_ModifyMyMeetingRoom = findViewById(R.id.button_modifyMyMeetingRoom)
        button_Logout = findViewById(R.id.button_logout)

        button_Notification.setOnClickListener(ButtonListener1())
        button_ModifyProfile.setOnClickListener(ButtonListener1())
        button_ModifyMyMeetingRoom.setOnClickListener(ButtonListener1())
        button_Logout.setOnClickListener(ButtonListener2())


    }

    // 로그인 하지 않은 경우 초기화 함수
    fun initData_NotSigned(){ // 로그인 하지 않은 경우 초기화 함수
        setContentView(R.layout.profile_not_signed_in)

        userImage = findViewById(R.id.userImage2)

        button_LogIn = findViewById(R.id.button_LogIn)

        button_LogIn.setOnClickListener(ButtonListener2())
    }

    private fun showProfile() {
        showProfileImage() // 프로필 사진 불러오기
        loadUserProfile() // 프로필 정보 불러오기
    }

    private fun signIn() {
        auth.signInWithEmailAndPassword("user01@gmail.com","pw01_1234")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    println("Sign in successful")
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("uid", "l9HyB6XFUpdYqFSHK6Q4")
                    startActivity(intent)
                    //showProfile()
                } else {
                    println("Sign in failed: ${task.exception?.message}")
                }
            }
    }

    private fun logOut() {
        auth.signOut()
        // 유저 프로필 정보 초기화
        initData_NotSigned()
    }

    private fun logIn() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showProfileImage() { // 프로필 사진 불러오기
        val user = auth.currentUser
        val photoUrl = storage.getReference("user/${currentUserUid}/profile.PNG").getDownloadUrl()
        if (user != null && photoUrl != null) {
            photoUrl.addOnSuccessListener {
                    uri ->
                Glide.with(userImage.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(userImage)
            }.addOnFailureListener { exception ->
                println("Error downloading profile image: ${exception.message}")
            }
        }
    }


    private fun loadUserProfile() {

        db.collection("users").document(currentUserUid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userProfile = document.toObject(UserProfile::class.java)
                    userProfile?.let {
                        nickName.text = it.name
                        birth.text = it.birth
                        userLocation.text = it.location
                        userMessage.text = it.comment
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error loading profile information: ${exception.message}")
            }
    }

    private fun editProfileFields(enable: Boolean) {
        nickName.isEnabled = enable
        birth.isEnabled = enable
        userLocation.isEnabled = enable
        userMessage.isEnabled = enable
    }

    private fun updateProfileInfo() {

        val updatedProfile = UserProfile(
            nickName.text.toString(),
            birth.text.toString(),
            userLocation.text.toString(),
            userMessage.text.toString()
        )

        db.collection("users").document(currentUserUid).set(updatedProfile)
            .addOnSuccessListener {

                button_ModifyProfile.tag = "저장"
                editProfileFields(false)
                loadUserProfile()
            }
            .addOnFailureListener {
                    exception -> println("Error updating user profile: ${exception.message}")
            }
    }

    private fun modifyProfile() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            // 이미지 URI 받기
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                // Firebase Storage에 사진 업로드
                val user = auth.currentUser
                if (user != null) {
                    val storageRef = storage.reference.child("users/${user.uid}/profile.PNG")
                    storageRef.putFile(selectedImageUri)
                        .addOnSuccessListener {
                            // 유저 프로필 이미지 URL 업로드
                            storageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    val profileUpdates = userProfileChangeRequest {
                                        photoUri = uri
                                    }
                                    user.updateProfile(profileUpdates)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // 새 프로필 이미지
                                                showProfileImage()
                                            } else {
                                                println("Error updating user profile: ${task.exception?.message}")
                                            }
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    println("Error getting download URL: ${exception.message}")
                                }
                        }
                        .addOnFailureListener { exception ->
                            println("Error uploading profile image: ${exception.message}")
                        }
                }
            }
        }
    }

    inner class ButtonListener1 : View.OnClickListener{ // 알림, 프로필 수정 버튼
        override fun onClick(v: View?) {
            when(v?.id){
                // 알림 버튼 클릭 시 알림 화면으로 이동 (profile_signed_in)
                //R.id.button_notification -> startActivity(Intent(ProfileActivity, ::class.java))

                // 프로필 수정 버튼 클릭 시 프로필 수정 (profile_signed_in)
                //R.id.button_modifyProfile ->
            }
        }
    }

    inner class ButtonListener2 : View.OnClickListener{ // 로그인, 로그아웃, 회원가입, 모임 관리 버튼
        override fun onClick(v: View?) {

            when(v?.id){

                // 로그인 버튼 클릭 시 로그인 화면으로 이동 (profile_not_signed_in)
                R.id.button_LogIn -> logIn()

                // 로그아웃 버튼 클릭 시 레이아웃 변경 및 프로필 정보 초기화(profile_signed_in)
                R.id.button_logout -> logOut()

                // 내 모임 관리 버튼 클릭 시 (profile_signed_in)
                //R.id.button_modifyMyMeetingRoom ->
            }
        }
    }
}