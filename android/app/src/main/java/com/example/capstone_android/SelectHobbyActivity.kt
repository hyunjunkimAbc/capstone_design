package com.example.capstone_android

import android.app.Activity
import android.graphics.Camera
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.databinding.ActivityHomeBinding
import com.example.capstone_android.databinding.ActivitySelecthobbyBinding
import com.example.capstone_android.hobbyfragment.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SelectHobbyActivity:AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivitySelecthobbyBinding
     var myhobbylist: ArrayList<String> = ArrayList<String>()
    val allhobbylist = arrayListOf<String>(
        "농구", "축구", "탁구", "테니스", "배드민턴",
        "야구", "볼링", "자전거", "골프", "런닝",
        "수영", "배구", "요가|필라테스", "태권도|유도", "복싱",
        "무술", "승마", "헬스", "롤러|보드", "스키|보드", "당구","등산","수상레저",
        "세계여행","국내여행","밴드","피아노","드럼","바이올린","기타","노래","작곡","힙합"
    ,"버스킹","콘서트","디제잉","런치패드","색소폰","친구","카페","술 한잔","코노","맛집탐방","봉사활동"
    ,"독서","글쓰기","토론","한식","일식","중식","양식","제과제빵","칵테일","와인","사진","영상제작"
    ,"AOS","RPG","FPS","카드게임","두뇌심리","스포츠게임","레이싱게임","닌텐도|플스","팝핀","비보잉","락킹"
    ,"왁킹","힙합댄스","하우스","크럼프","현대무용","한국무용","K-POP","발레","댄스스포츠","발리댄스","재즈","에어로빅"
    ,"자동차","오토바이","강아지","고양이","고슴도치","햄스터","물고기","앵무새","다람쥐","도마뱀","뱀","거미"
    ,"미술","공방","도예","자수","꽃","화장품","가구","스터디","언어","동기부여","스피치"
    ,"IT","디자인","의료","화학","금융","건설","법","패션"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelecthobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        val key=intent.getStringExtra("key")
        println(key)
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.textview.text="관심사 재설정"

        /*
        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { document ->
                val item = document.toObject(SignUpData::class.java)
                for (data in item?.interest_array!!) {
                    myhobbylist.add(data)
                }
            }
         */
        val userinfo=SingleTonData.userInfo
        for(data in userinfo?.interest_array!!)
            myhobbylist.add(data)

        binding.sportsImageButton.setOnClickListener{
            val sportfragment=SportFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            sportfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,sportfragment).commit()
        }
        binding.tripImageButton.setOnClickListener{
            val tripfragment=TripFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            tripfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,tripfragment).commit()
        }
        binding.musicImageButton.setOnClickListener{
            val musicfragment=MusicFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            musicfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,musicfragment).commit()
        }
        binding.societyImageButton.setOnClickListener{
            val societyfragment=SocietyFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            societyfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,societyfragment).commit()
        }
        binding.readImageButton.setOnClickListener{
            val bookfragment=BookFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            bookfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,bookfragment).commit()
        }
        binding.cookImageButton.setOnClickListener{
            val cookfragment=CookFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            cookfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,cookfragment).commit()
        }
        binding.photoImageButton.setOnClickListener{
            val camerafragment=CameraFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            camerafragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,camerafragment).commit()
        }
        binding.gameImageButton.setOnClickListener{
            val gamefragment=GameFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            gamefragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,gamefragment).commit()
        }
        binding.danceImageButton.setOnClickListener{
            val dancefragment=DanceFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            dancefragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,dancefragment).commit()
        }
        binding.carImageButton.setOnClickListener{
            val carfragment=CarFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            carfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,carfragment).commit()
        }
        binding.petImageButton.setOnClickListener{
            val petfragment=PetFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            petfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,petfragment).commit()
        }
        binding.artImageButton.setOnClickListener{
            val artfragment=ArtFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            artfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,artfragment).commit()
        }
        binding.studyImageButton.setOnClickListener{
            val studyfragment=StudyFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            studyfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,studyfragment).commit()
        }
        binding.jobImageButton.setOnClickListener{
            val jobfragment=JobFragment()
            val bundle=Bundle()
            bundle.putString("key",key)
            jobfragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.categoryfragment,jobfragment).commit()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.changehobby_toolbar,menu)
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
                println(myhobbylist)
                val data = hashMapOf("interest_array" to myhobbylist)
                db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).set(data, SetOptions.merge())
                SingleTonData.userInfo?.interest_array=myhobbylist
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
            R.id.cancel->{
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        println(myhobbylist)
        super.onDestroy()
    }
}