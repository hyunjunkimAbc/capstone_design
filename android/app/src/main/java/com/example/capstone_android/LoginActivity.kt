package com.example.capstone_android

import android.app.Application
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class LoginActivity : AppCompatActivity() { // 로그인 화면
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    val db = Firebase.firestore
    val st = FirebaseStorage.getInstance()
    var myNickName =""
    val userProfiles = db.collection("user")
    //var timer = Timer()
    //var timeCnt = 0

    // 구글 로그인을 위한 객체
    private var googleSignInClient : GoogleSignInClient?=null
    // FireBase에서 계정 정보를 가져오는 객체
    lateinit var auth : FirebaseAuth
    // 구글 로그인에 필요한 변수 설정 onActivityResultCode 를 위한것임
    private val RC_SIGN_IN = 9001


/*
    fun activateTimer(){
        //타이머 동작 시간 지정 및 작업 내용 지정
        timer.schedule(object : TimerTask(){
            override fun run(){
                //카운트 값 증가
                timeCnt ++
                if(timeCnt > 30){
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "30초가 넘었습니다. 네트워크 상태를 확인해 보세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    timer.cancel()
                }
            }
        },1000, 1000) //1초뒤 실행, 1초 마다 반복
    }
    fun inActivateTimer(){
        Toast.makeText(
            this@LoginActivity,
            "로그인을 성공했습니다.",
            Toast.LENGTH_SHORT
        ).show()
        timer.cancel()
    }
    fun inActivateTimer2(){
        Toast.makeText(
            this@LoginActivity,
            "로그인을 실패했습니다.",
            Toast.LENGTH_SHORT
        ).show()
        timer.cancel()
    }
 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // GoogleSigninOptional
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        //val signInGoogleBtn : SignInButton = binding.googleSignInButton
        binding.googleSignInButton.setOnClickListener {
            println("구글 로그인 버튼 클릭")
            googleLogin() // 로그인 처리
        }

        binding.LoginButton.setOnClickListener(){
            println("로그인 버튼 클릭")
            if(binding.LoginEmailEditText.getText().toString()=="") {    // nickname을 입력하지 않았을 때
                println("이메일을 입력하지않음")

                Toast.makeText(
                    this,
                    "이메일을 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(binding.LoginPWEditText.getText().toString()=="") {    // nickname을 입력하지 않았을 때
                println("비밀번호를 입력하지않음")
                Toast.makeText(
                    this,
                    "비밀번호를 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //activateTimer()// 로그인 시간 30초이하로 성공 타이머 종료
                Firebase.auth.signInWithEmailAndPassword(
                    binding.LoginEmailEditText.getText().toString(),
                    binding.LoginPWEditText.getText().toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //inActivateTimer() // 로그인 시간 30초이하로 성공 타이머 종료
                            userProfiles.get().addOnSuccessListener {
                                for (user in it) {
                                    if ("${user["email"]}" == binding.LoginEmailEditText.getText()
                                            .toString()
                                    ) {
                                        myNickName = "${user["nickname"]}"
                                        Toast.makeText(
                                            this,
                                            "${myNickName}님 환영합니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                println("로그인 성공")
                                // 로그인 성공을 알리는 스낵바
                                Toast.makeText(
                                    this,
                                    "로그인에 성공하였습니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this, HomeActivity::class.java)
//                                intent.putExtra("userNickName", myNickName)
                                startActivity(intent)
                                // 로그인 성공을 알리는 팝업창
                                /*val builder = AlertDialog.Builder(this)
                                builder.setTitle("로그인 성공")
                                    .setMessage("로그인에 성공하였습니다.")
                                    .setPositiveButton(
                                        "확인",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            // 게시물 리스트 화면으로 이동 (PostingsActivity)
                                            *//*val intent = Intent(this, PostingsActivity::class.java)
                                            intent.putExtra("userNickName", myNickName)
                                            startActivity(intent)

                                             *//*
                                        })
                                builder.create()
                                builder.show()*/
                            }
                        } else {
                            //로그인 실패 오류 출력
                            println("로그인 실패 ${it.exception?.message}")
                            // 로그인 실패를 알리는 팝업창
                            //inActivateTimer2() // 로그인 시간 30초이하로 실패 타이머 종료
                            Toast.makeText(
                                this,
                                "이메일 혹은 비밀번호가 틀렸습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        //findViewById<Button>(R.id.ToSignupButton).setOnClickListener(){
        binding.ToSignupButton.setOnClickListener(){
            println("회원가입 버튼 클릭")
            //회원가입 화면으로 이동 (SignUpActivity)
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    // onStart. 유저가 앱에 이미 구글 로그인을 했는지 확인
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account !== null) { // 이미 로그인 되어있을시 바로 메인 액티비티로 이동
            //MainActivity(firebaseAuth.currentUser)
        }
    }

    // 구글에 인증하는 부분
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }
            catch (e: ApiException){
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*
    var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
    if(result!!.isSuccess){
        var account = result.signInAccount
        firebaseAuthWithGoogle(account)*/


    fun googleLogin(){
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    //Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {    // 구글 로그인 성공
                    println("구글 로그인 인증 성공")

                    // 구글 로그인 : user DB 정보 설정
                    val user = auth.currentUser
                    setUserInfo(user)


//                    // 프로필 이미지가 선택되었으면
//                    if (selected_profile_img==1) {
//                        // Firebase Storage로 이미지 전송
//                        imgName = "${user?.uid ?: String}"
//                        var storageReference =
//                            st?.reference?.child("user_profile_image")?.child(imgName!!)
//
//                        storageReference?.putFile(image!!)?.addOnSuccessListener {
//                            println("이미지 업로드 성공")
//                        }
//                    }
                    moveMainPage(auth?.currentUser) // 홈화면으로 이동
                } else { // 구글 로그인 실패
                    println("구글 로그인 인증 실패")
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    // 구글 로그인 : 정보 설정
    fun setUserInfo(user: FirebaseUser?) {
        val col = db.collection("user")
        user?.let{
            val itemMap = hashMapOf(
                "email" to user.email,
//                        "password" to binding.PWEditText.getText().toString(),
                "nickname" to user.displayName,
                "uid" to user.uid,
//                            "birthday" to datePicker.year.toString()+datePicker.month.toString()+datePicker.dayOfMonth.toString(),
//                            "interest" to selected_interest,
                "edit_time" to System.currentTimeMillis(),
                "profile_message" to ""
            )
            col.add(itemMap)

            // 구글 로그인 : 프로필 이미지 storage에 전송
            var imgName = "${user?.uid ?: String}"
            var storageReference =
                st?.reference?.child("user_profile_image")?.child(imgName!!)
            storageReference?.putFile(user.photoUrl!!)?.addOnSuccessListener {
                println("이미지 업로드 성공")
            }
        }
    }

    fun moveMainPage(user: FirebaseUser?){ // 홈화면으로 이동
        if(user!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseAuthSignOut()
    }

    private fun firebaseAuthSignOut() { // 로그아웃 시키기
        Firebase.auth.signOut()
    }
}