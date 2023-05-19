package com.example.capstone_android

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class LoginActivity : AppCompatActivity() { // 로그인 화면
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    val db = Firebase.firestore
    var myNickName =""
    val userProfiles = db.collection("user")
    //var timer = Timer()
    //var timeCnt = 0

    // email과 비밀번호의 동적 TextView를 구분하기위함
    var textView_num:Int? = null

    // 동적 TextView를 띄우기 위한 공간
    lateinit var listView1: LinearLayout
    lateinit var listView2: LinearLayout

    // 구글 로그인을 위한 객체
    private var googleSignInClient : GoogleSignInClient?=null
    // FireBase에서 계정 정보를 가져오는 객체
    lateinit var auth : FirebaseAuth
    // 구글 로그인에 필요한 변수 설정 onActivityResultCode 를 위한것임
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listView1 = binding.container1
        listView2 = binding.container2

        // 구글 로그인 버튼 텍스트 세팅
        setGoogleButtonText(binding.googleSignInButton,"Google 계정으로 로그인")

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
            if(binding.LoginEmailEditText.getText().toString()=="") {    // 이메일을 입력하지않음
                // email_editText 아래 입력요청 문구를 동적으로 생성
                dynamicText(0)
            }
            else if(binding.LoginPWEditText.getText().toString()=="") {    // 비밀번호를 입력하지않음
                // PW_editText 아래 입력요청 문구를 동적으로 생성
                dynamicText(1)
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
                                        SingleTonData.userInfo=user.toObject(SignUpData::class.java)
                                        myNickName = "${user["nickname"]}"
                                        Toast.makeText(
                                            this,
                                            "${myNickName}님 환영합니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                println("로그인 성공")

                                // 홈화면으로 이동
                                val intent = Intent(this, ConciergeActivity::class.java)
                                intent.putExtra("userNickName", myNickName)
                               startActivity(intent)

                            }
                        } else {
                            //로그인 실패 오류 출력
                            println("로그인 실패 ${it.exception?.message}")
                            // 로그인 실패를 알리는 팝업창
                            //inActivateTimer2() // 로그인 시간 30초 이하로 실패 타이머 종료
                            Toast.makeText(
                                this,
                                "이메일 혹은 비밀번호가 틀렸습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        binding.SignupTextView.setOnClickListener(){
            println("회원가입 클릭")
            // 회원가입 화면으로 이동
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.SetPWTextView.setOnClickListener(){
            // 비밀번호 재설정 화면으로 이동
            val intent = Intent(this, ResetPWActivity::class.java)
            startActivity(intent)
        }
    }

    // 구글 로그인 버튼 텍스트 세팅
    private fun setGoogleButtonText(loginButton: SignInButton, buttonText: String){
        var i = 0
        while (i < loginButton.childCount){
            var v = loginButton.getChildAt(i)
            if (v is TextView) {
                var tv = v
                tv.setText(buttonText)
                tv.setGravity(Gravity.CENTER)
                return
            }
            i++
        }
    }


    // onStart. 유저가 앱에 이미 구글 로그인을 했는지 확인
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account !== null) { // 이미 로그인 되어있을시 바로 홈 액티비티로 이동
//            MainActivity(firebaseAuth.currentUser)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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
//                    setUserInfo(user) // 구글 계정의 개인정보를 DB에 세팅
                    moveSetPage(user) // 구글 계정의 개인정보 설정화면으로 이동 (추가적인 개인정보 설정을 위함)
                } else { // 구글 로그인 실패
                    println("구글 로그인 인증 실패")
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    // 구글 로그인 : 정보 설정
    fun setUserInfo(user: FirebaseUser?) {
        val col = db.collection("user").document("${user?.uid}")
        user?.let{
            val itemMap = hashMapOf(
                "email" to user.email,
                "nickname" to user.displayName,
                "uid" to user.uid,
                "edit_time" to System.currentTimeMillis(),
                "profile_message" to "",
                "birthday" to "",
                "interest_array" to ""
            )
            col.set(itemMap)
        }
    }

    fun moveSetPage(user: FirebaseUser?){ // 구글로그인 시 개인정보 설정 화면으로 이동
        if(user!=null){
            val intent = Intent(this, SetGoogleAccountActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseAuthSignOut()
    }

    private fun firebaseAuthSignOut() { // 로그아웃 시키기
        Firebase.auth.signOut()
    }

    // editText 아래에 입력요청 text를 동적으로 생성
    private fun dynamicText(textView_num:Int) {
        val dynamicTextView : TextView = TextView(applicationContext)
        listView1.removeAllViews()
        listView2.removeAllViews()
        dynamicTextView.textSize = 16f
        dynamicTextView.setTextColor(Color.RED)
        dynamicTextView.setTypeface(null, Typeface.BOLD)
        dynamicTextView.id = 0
        val param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        param.marginStart = 30
        dynamicTextView.layoutParams = param
//        dynamicTextView.setBackgroundColor(Color.rgb(184, 236, 184))
        if(textView_num==0) {
            dynamicTextView.setText("Email을 입력해주세요!")
            listView1.addView(dynamicTextView)
        }
        else if(textView_num==1){
            dynamicTextView.setText("비밀번호를 입력해주세요!")
            listView2.addView(dynamicTextView)
        }
    }
}