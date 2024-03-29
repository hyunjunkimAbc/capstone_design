package com.example.capstone_android
import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstone_android.MeetingList.MapFragment
import com.example.capstone_android.MeetingList.MeetingViewModel
import com.example.capstone_android.Util.MainMenuId
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.naver.maps.map.NaverMap

class HomeActivity: AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    var checkfragment:Number=0
    var detailViewFragment:DetailViewFragment?=null
    var mapFragment: MapFragment?=null
    var profileFragment :ProfileFragment? = null
    var UserReservationListFragment :UserReservationListFragment? = null
    var ReservationListFragment :ReservationListFragment? =null
    var myApplyCompetitionFragment: MyApplyCompetitionFragment? =null
    var myCreateCompetitionFragment:MyCreateCompetition? =null

    var openkey:String?=null
    private lateinit var viewModel: MeetingViewModel
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= Firebase.firestore
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        binding.textview.setOnClickListener{
            val intent=Intent(this,AddressActivity::class.java)
            startActivityForResult(intent,100)
        }

        openkey=intent.getStringExtra("openkey")
        Log.d(ContentValues.TAG,"키는 ${openkey}")
        binding.textview.text=SingleTonData.userInfo?.address


        detailViewFragment=DetailViewFragment()
        var bundle=Bundle()
        bundle.putString("openkey",openkey)
        detailViewFragment?.arguments=bundle
        supportFragmentManager.beginTransaction().replace(R.id.search_content,detailViewFragment!!).commit()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
        binding.bottomNavigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.action_home ->{
                    if(detailViewFragment==null){
                        detailViewFragment= DetailViewFragment()
                        supportFragmentManager.beginTransaction().add(R.id.search_content,detailViewFragment!!).commit()
                    }
                    if(detailViewFragment!=null)
                        supportFragmentManager.beginTransaction().show(detailViewFragment!!).commit()
                    if(mapFragment!=null)
                        supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
                    if(profileFragment!=null)
                        supportFragmentManager.beginTransaction().hide(profileFragment!!).commit()
                    if(UserReservationListFragment!= null)
                        supportFragmentManager.beginTransaction().hide(UserReservationListFragment!!).commit()
                    if(ReservationListFragment!= null)
                        supportFragmentManager.beginTransaction().hide(ReservationListFragment!!).commit()
                    if(myApplyCompetitionFragment!=null)
                        supportFragmentManager.beginTransaction().hide(myApplyCompetitionFragment!!).commit()
                    if(myCreateCompetitionFragment!=null)
                        supportFragmentManager.beginTransaction().hide(myCreateCompetitionFragment!!).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_gps ->{
                    if(mapFragment==null){
                        mapFragment= MapFragment()
                        val bundle2=Bundle()
                        bundle2.putString("openkey",openkey)
                        mapFragment?.arguments=bundle2
                        supportFragmentManager.beginTransaction().add(R.id.search_content,mapFragment!!).commit()
                    }
                    if(mapFragment!=null)
                        supportFragmentManager.beginTransaction().show(mapFragment!!).commit()
                    if(detailViewFragment!=null)
                        supportFragmentManager.beginTransaction().hide(detailViewFragment!!).commit()
                    if(profileFragment!=null)
                        supportFragmentManager.beginTransaction().hide(profileFragment!!).commit()
                    if(UserReservationListFragment!= null)
                        supportFragmentManager.beginTransaction().hide(UserReservationListFragment!!).commit()
                    if(ReservationListFragment!= null)
                        supportFragmentManager.beginTransaction().hide(ReservationListFragment!!).commit()
                    if(myApplyCompetitionFragment!=null)
                        supportFragmentManager.beginTransaction().hide(myApplyCompetitionFragment!!).commit()
                    if(myCreateCompetitionFragment!=null)
                        supportFragmentManager.beginTransaction().hide(myCreateCompetitionFragment!!).commit()
                }

                R.id.action_account ->{
                    if(profileFragment ==null){
                        profileFragment= ProfileFragment()
                        supportFragmentManager.beginTransaction().add(R.id.search_content,profileFragment!!).commit()
                    }
                    if(profileFragment!=null)
                        supportFragmentManager.beginTransaction().show(profileFragment!!).commit()
                    if(detailViewFragment!=null)
                        supportFragmentManager.beginTransaction().hide(detailViewFragment!!).commit()
                    if(mapFragment!=null)
                        supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
                    if(UserReservationListFragment!= null)
                        supportFragmentManager.beginTransaction().hide(UserReservationListFragment!!).commit()
                    if(ReservationListFragment!= null)
                        supportFragmentManager.beginTransaction().hide(ReservationListFragment!!).commit()
                    if(myApplyCompetitionFragment!=null)
                        supportFragmentManager.beginTransaction().hide(myApplyCompetitionFragment!!).commit()
                    if(myCreateCompetitionFragment!=null)
                        supportFragmentManager.beginTransaction().hide(myCreateCompetitionFragment!!).commit()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onDestroy() {
        print("홈 액티비티 종료")
        super.onDestroy()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100) {
            binding.textview.text = SingleTonData.userInfo?.address
            if (openkey == MainMenuId.periodic) {
                detailViewFragment?.update()
                if(mapFragment!=null){
                    mapFragment?.removeAllMarkers()
                    mapFragment?.addAllMarkers()
                }
            } else if (openkey == MainMenuId.light) {
                detailViewFragment?.updatelight()
                if(mapFragment!=null){
                    mapFragment?.removeAllMarkers()
                    mapFragment?.addAllMarkers()
                }
            }
            else if(openkey==MainMenuId.place){
                detailViewFragment?.updateplace()
                if(mapFragment!=null){
                    mapFragment?.removeAllMarkers()
                    mapFragment?.addAllMarkers()
                }
            }
            else if(openkey==MainMenuId.competition){
                detailViewFragment?.updateCompetition()
                if(mapFragment!=null){
                    mapFragment?.removeAllMarkers()
                    mapFragment?.addAllMarkers()
                }
            }
        }

    }

    fun addremovemapmarker(){
        if(mapFragment!=null) {
            mapFragment?.removeAllMarkers()
            mapFragment?.addAllMarkers()
        }
    }
    fun updateaddress(){
        binding.textview.text = SingleTonData.userInfo?.address
        if(detailViewFragment!=null){
            detailViewFragment?.updateaddress()
        }
        if(mapFragment!=null) {
            mapFragment?.removeAllMarkers()
            mapFragment?.addAllMarkers()
        }
    }
    fun homeupdateinterest(){
        if(detailViewFragment!=null){
            detailViewFragment?.update_interest()
            detailViewFragment?.updateaddress()
        }
        if(mapFragment!=null) {
            mapFragment?.removeAllMarkers()
            mapFragment?.addAllMarkers()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun gotoReservationListFragment(){
        /*
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_content, UserReservationListFragment())
            .commit()*/
        if(UserReservationListFragment ==null){
            UserReservationListFragment= UserReservationListFragment()
            supportFragmentManager.beginTransaction().add(R.id.search_content,UserReservationListFragment!!).commit()
        }
        if(UserReservationListFragment!=null)
            supportFragmentManager.beginTransaction().show(UserReservationListFragment!!).commit()
        if(detailViewFragment!=null)
            supportFragmentManager.beginTransaction().hide(detailViewFragment!!).commit()
        if(mapFragment!=null)
            supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
        if(profileFragment!= null)
            supportFragmentManager.beginTransaction().hide(profileFragment!!).commit()
        if(ReservationListFragment!= null)
            supportFragmentManager.beginTransaction().hide(ReservationListFragment!!).commit()
        if(myApplyCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().hide(myApplyCompetitionFragment!!).commit()
        if(myCreateCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().hide(myCreateCompetitionFragment!!).commit()
    }
    fun gotoReservationAllowFragment(){
        /*
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_content, UserReservationListFragment())
            .commit()*/
        if(ReservationListFragment ==null){
            ReservationListFragment= ReservationListFragment()
            supportFragmentManager.beginTransaction().add(R.id.search_content,ReservationListFragment!!).commit()
        }
        if(ReservationListFragment!=null)
            supportFragmentManager.beginTransaction().show(ReservationListFragment!!).commit()
        if(detailViewFragment!=null)
            supportFragmentManager.beginTransaction().hide(detailViewFragment!!).commit()
        if(mapFragment!=null)
            supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
        if(profileFragment!= null)
            supportFragmentManager.beginTransaction().hide(profileFragment!!).commit()
        if(UserReservationListFragment!=null)
            supportFragmentManager.beginTransaction().hide(UserReservationListFragment!!).commit()
        if(myApplyCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().hide(myApplyCompetitionFragment!!).commit()
        if(myCreateCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().hide(myCreateCompetitionFragment!!).commit()
    }
    fun gotoCompetitionFragment(){
        /*
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_content, UserReservationListFragment())
            .commit()*/
        if(myApplyCompetitionFragment ==null){
            myApplyCompetitionFragment= MyApplyCompetitionFragment()
            supportFragmentManager.beginTransaction().add(R.id.search_content,myApplyCompetitionFragment!!).commit()
        }
        if(myApplyCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().show(myApplyCompetitionFragment!!).commit()
        if(detailViewFragment!=null)
            supportFragmentManager.beginTransaction().hide(detailViewFragment!!).commit()
        if(mapFragment!=null)
            supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
        if(profileFragment!= null)
            supportFragmentManager.beginTransaction().hide(profileFragment!!).commit()
        if(UserReservationListFragment!=null)
            supportFragmentManager.beginTransaction().hide(UserReservationListFragment!!).commit()
        if(ReservationListFragment!=null)
            supportFragmentManager.beginTransaction().hide(ReservationListFragment!!).commit()
        if(myCreateCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().hide(myCreateCompetitionFragment!!).commit()
    }
    fun gotoCompetitionCreateFragment(){
        /*
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_content, UserReservationListFragment())
            .commit()*/
        if(myCreateCompetitionFragment ==null){
            myCreateCompetitionFragment= MyCreateCompetition()
            supportFragmentManager.beginTransaction().add(R.id.search_content,myCreateCompetitionFragment!!).commit()
        }
        if(myCreateCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().show(myCreateCompetitionFragment!!).commit()
        if(detailViewFragment!=null)
            supportFragmentManager.beginTransaction().hide(detailViewFragment!!).commit()
        if(mapFragment!=null)
            supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
        if(profileFragment!= null)
            supportFragmentManager.beginTransaction().hide(profileFragment!!).commit()
        if(UserReservationListFragment!=null)
            supportFragmentManager.beginTransaction().hide(UserReservationListFragment!!).commit()
        if(ReservationListFragment!=null)
            supportFragmentManager.beginTransaction().hide(ReservationListFragment!!).commit()
        if(myApplyCompetitionFragment!=null)
            supportFragmentManager.beginTransaction().hide(myApplyCompetitionFragment!!).commit()
    }
}