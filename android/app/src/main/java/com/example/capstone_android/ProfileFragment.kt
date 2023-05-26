package com.example.capstone_android

//import com.example.capstone_android.SearchAddress.SearchMap
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_edit_posting.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_signed_in, container, false)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserUid: String
    private var storage = FirebaseStorage.getInstance()
    private var db = FirebaseFirestore.getInstance()

    private lateinit var locationForUpload : String
    private var locationxForUpload : Long = 0
    private var locationyForUpload: Long = 0
    private var editTimeForUpload: Long = System.currentTimeMillis()

    private lateinit var userImage : ImageView

    private lateinit var nickName : EditText
    private lateinit var birth : EditText
    private lateinit var userMessage : EditText
    private lateinit var userPhoneNumber: TextView

    private lateinit var button_ModifyProfile : Button
    private lateinit var button_userLocation : Button
    private lateinit var button_MyApplyCompetition : Button
    private lateinit var button_MyCompetition : Button
    private lateinit var button_MyMeetingRoom : Button
    private lateinit var button_MyPlaceRental : Button
    private lateinit var button_LogOut : Button
    private lateinit var button_LogIn : Button

    companion object {
        const val REQUEST_IMAGE = 100
        const val REQUEST_ADDRESS = 101
        private const val ARG_UID = "uid"

        fun newInstance(intent: Intent): ProfileFragment {
            val uid = intent.getStringExtra("uid")
            val args = Bundle().apply {
                putString(ARG_UID, uid)
            }
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    data class UserProfile( // 프로필 정보
        var nickname: String = "이름", // 이름
        var birthday: String = "생년월일", // 생년월일
        var address: String = "나의 위치", // 위치정보
        var locationx: Long = 0, // 위도
        var locationy: Long = 0, // 경도
        var email: String = "이메일", // 전화번호
        var profile_message: String = "자기소개", // 자기소개글
        var edit_time: Long = 0 // 회원정보수정시간
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        storage = Firebase.storage

        val currentUser = auth.currentUser

        if (currentUser != null) { // 로그인 한 경우
            println("Sign in")
            currentUserUid = currentUser.uid
            initData_Signed(view)
            editProfileFields(false) // 프로필 정보 변경 비활성화
            showProfile()

            // 정보 수정 버튼 설정
            button_ModifyProfile.setOnClickListener {
                // "수정"이면 프로필 수정 가능하게 변경
                if (button_ModifyProfile.text == "수정") {

                    button_ModifyProfile.text = "저장"
                    editProfileFields(true) // 프로필 정보 변경 활성화
                } else {
                    // "저장"이면 프로필 정보 업데이트해서 반영
                    updateProfileInfo()
                    editProfileFields(false) // 프로필 정보 변경 비활성화
                    button_ModifyProfile.text = "수정"
                }
            }


        } else { // 로그인 하지 않은 경우
            initData_NotSigned(view)
            println("NOT Sign in")
        }

    }

    // 로그인 한 경우 초기화 하는 함수
    fun initData_Signed(rootView: View){ // 로그인 한 경우 초기화 함수
        userImage = rootView.findViewById(R.id.userImage1)
        nickName = rootView.findViewById(R.id.nickname)
        birth = rootView.findViewById(R.id.birth)
        userMessage = rootView.findViewById(R.id.introduce_comment)
        userPhoneNumber = rootView.findViewById(R.id.phone_number)

        button_ModifyProfile = rootView.findViewById(R.id.button_modifyProfile)
        button_userLocation = rootView.findViewById(R.id.button_mylocation)
        button_MyApplyCompetition = rootView.findViewById(R.id.button_myApplyCompetition)
        button_MyCompetition = rootView.findViewById(R.id.button_myCompetition)
        button_MyMeetingRoom = rootView.findViewById(R.id.button_myMeetingRoom)
        button_MyPlaceRental = rootView.findViewById(R.id.button_myPlaceRental)
        button_LogOut = rootView.findViewById(R.id.button_logOut)


        button_ModifyProfile.setOnClickListener(ButtonListener())
        button_userLocation.setOnClickListener(ButtonListener())
        button_MyApplyCompetition.setOnClickListener(ButtonListener())
        button_MyCompetition.setOnClickListener(ButtonListener())
        button_MyMeetingRoom.setOnClickListener(ButtonListener())
        button_MyPlaceRental.setOnClickListener(ButtonListener())
        button_LogOut.setOnClickListener(ButtonListener())

    }

    // 로그인 하지 않은 경우 초기화 및 로그인 화면으로 이동하는 함수
    fun initData_NotSigned(rootView: View){ // 로그인 하지 않은 경우 초기화 함수
        userImage = rootView.findViewById(R.id.userImage2)
        logIn()
    }

    private fun showProfile() {
        showProfileImage() // 프로필 사진 불러오기
        loadUserProfile() // 프로필 정보 불러오기
    }

    private fun logOut() { // 로그아웃 시 실행 함수. 로그아웃 하고 로그인 화면으로 넘어감
        auth.signOut()
        // 로그인 화면으로 넘기기
        logIn()
    }

    private fun logIn() { // 로그인 화면으로 이동
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 스택을 제거
        startActivity(intent)
    }

    private fun showProfileImage() { // 프로필 사진 불러오기
        val user = auth.currentUser
        val storageReference = storage.reference
        var photoReference = storageReference.child("user_profile_image/${currentUserUid}.jpg")

        if (user != null && photoReference != null) {
            photoReference.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                if (it.isSuccessful) { // 사용자 설정 프로필 이미지
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    userImage.setImageBitmap(bmp)
                } else { // 기본 설정 프로필 이미지
                    photoReference = storageReference.child("user_profile_image/default.jpg")

                    photoReference.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                        if(it.isSuccessful){
                            val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                            userImage.setImageBitmap(bmp)
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

    private fun loadUserProfile() { // 데이터베이스에서 유저 프로필 정보 받아오기
        db.collection("user").document(currentUserUid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userProfile = document.toObject(UserProfile::class.java)
                    userProfile?.let {
                        nickName.setText(it.nickname)
                        birth.setText(it.birthday)
                        button_userLocation.setText(it.address)
                        userPhoneNumber.setText(it.email)
                        userMessage.setText(it.profile_message)
                        editTimeForUpload
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error loading profile information: ${exception.message}")
            }
    }

    private fun loadUserAddress() { // 데이터베이스에서 유저 주소 정보 받아오기
        db.collection("user").document(currentUserUid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userProfile = document.toObject(UserProfile::class.java)
                    userProfile?.let {
                        button_userLocation.setText(it.address)
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error loading profile information: ${exception.message}")
            }
    }

    private fun editProfileFields(enable: Boolean) { // true, false에 따라 값 변경 가능 여부 결정
        nickName.isEnabled = enable
        birth.isEnabled = enable
        button_userLocation.isEnabled = enable
        userPhoneNumber.isEnabled = enable
        userMessage.isEnabled = enable
    }

    private fun updateProfileInfo() { //
        editTimeForUpload = System.currentTimeMillis()
        var temp = button_userLocation.text.toString()
        var tempSplit = temp.split(" ")
        var uploadAddress = "${tempSplit[0]} ${tempSplit[1]}"
        val updatedProfile = UserProfile(
            nickName.text.toString(),
            birth.text.toString(),
            uploadAddress,
            locationxForUpload, // 위도 받아오기
            locationyForUpload, // 경도 받아오기
            userPhoneNumber.text.toString(),
            userMessage.text.toString(),
            editTimeForUpload
        )

        db.collection("user").document(currentUserUid).set(updatedProfile, SetOptions.merge())
            .addOnSuccessListener {
                //println("성공")
                //editProfileFields(false)
                //loadUserProfile()
            }
            .addOnFailureListener {
                    exception -> println("Error updating user profile: ${exception.message}")

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE && data != null) {
                // 이미지 URI 받기
                val selectedImageUri = data.data
                if (selectedImageUri != null) {
                    // Firebase Storage에 사진 업로드
                    val user = auth.currentUser
                    if (user != null) {
                        val storageRef = storage.reference.child("user_profile_image/${currentUserUid}.jpg")
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
            } else if (requestCode == REQUEST_ADDRESS && data != null) {
                // 주소 변경 액티비티로부터 넘어온 값 받기
                var address = data?.extras?.getString("address").toString()
                // 기존 버튼의 text와 넘어온 값이 다르면 갱신 해줘야 됨.
                if (button_userLocation.text.toString() != address) {
                    button_userLocation.setText("${address} (변경하려면 클릭하세요)")
                    button_userLocation.invalidate()
                    //updateProfileInfo()
                }
            }
        }


    }


    inner class ButtonListener : View.OnClickListener{ // 로그아웃, 회원가입, 모임 관리 버튼

        override fun onClick(v: View?) {
            val mActivity = activity as HomeActivity

            when(v?.id){
                // 나의 신청 대회 버튼 클릭 시 신청 대회 목록 확인하는 프래그먼트로 이동
                R.id.button_myApplyCompetition -> {
                    val detailViewFragment=MyApplyCompetitionFragment()
                    mActivity.supportFragmentManager.beginTransaction().replace(R.id.main_content,detailViewFragment).commit()
                }

                // 나의 개설 대회 버튼 클릭 시 개설 대회 목록 확인하는 프래그먼트로 이동
                /*R.id.button_myCompetition -> {
                    val detailViewFragment=MyCompetitionFragment()
                    mActivity.supportFragmentManager.beginTransaction().replace(R.id.main_content,detailViewFragment).commit()
                }*/

                // 내 모임 버튼 클릭 시 가입한 모임 목록 확인하는 프래그먼트로 이동
                /*R.id.button_myMeetingRoom -> {
                    val detailViewFragment=MyMeetingRoomFragment()
                    mActivity.supportFragmentManager.beginTransaction().replace(R.id.main_content,detailViewFragment).commit()
                }*/


                // 나의 장소 대여 버튼 클릭 시 대여한 장소 목록 확인하는 프래그먼트로 이동
                R.id.button_myPlaceRental -> {
                    (activity as HomeActivity).gotoReservationListFragment()
                }

                // 내 주소 버튼 클릭 시 주소 변경 화면으로 이동
                R.id.button_mylocation -> {
                    val intent = Intent(requireActivity(), SearchMap::class.java)
                    intent.apply {
                        this.putExtra("create","hello")
                    }
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) // 액티비티 애니메이션 없앰
                    startActivityForResult(intent, 101)
                }

                /*
                // 내 주소 버튼 클릭 시 주소 변경 화면으로 이동 (범학 테스트용)
                R.id.button_mylocation -> {
                    val intent = Intent(requireActivity(), ChangeDataTestActivity::class.java)
                    intent.apply {
                        this.putExtra("addressFromPF", button_userLocation.text)
                    }
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) // 액티비티 애니메이션 없앰
                    startActivityForResult(intent, 101)
                }*/

                // 로그아웃하고 로그인 화면으로 이동
                R.id.button_logOut -> logOut()
            }
        }
    }
}