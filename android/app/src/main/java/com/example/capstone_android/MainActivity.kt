
package com.example.capstone_android

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone_android.MainHomeActivity.BannerViewModel
import com.example.capstone_android.MainHomeActivity.ViewPagerAdapter
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.BannerItem
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewModel: BannerViewModel
    private lateinit var hobbydata:ArrayList<String>
    var address:String?=null
    var mainclubdata:ArrayList<ClubData> =arrayListOf()
    private var isRunning = true
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db= Firebase.firestore
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.homeUserAddress.setOnClickListener{
                val intent=Intent(this,AddressActivity::class.java)
                startActivityForResult(intent,10)
        }
        binding.Mainmeetingbutton.setOnClickListener{
            var intent= Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.mainhomeRefreshLayout.setOnRefreshListener{
            binding.mainhomeRefreshLayout.isRefreshing = false
        }
        viewModel = ViewModelProvider(this)[BannerViewModel::class.java]
        viewModel.setBannerItems(
            listOf(BannerItem(R.drawable.banner1), BannerItem(R.drawable.banner2), BannerItem(R.drawable.banner3), BannerItem(R.drawable.banner4),))
        initViewPager2()
        subscribeObservers()
        autoScrollViewPager()
        lifecycleScope.launch(Dispatchers.Main){
            try{
                RoadData()
                val userinfo=db.collection("user").document(Firebase.auth.currentUser?.uid.toString())
                val userhobby=userinfo.get().await().toObject(SignUpData::class.java)
                SingleTonData.userInfo=userhobby
                address=userhobby?.address
                hobbydata=userhobby?.interest_array!!
                viewModel.setuseraddress(address!!)
                for(data in hobbydata){
                    val roominfo=db.collection("meeting_room").whereEqualTo("category",data).whereEqualTo("address",address).get().await()
                    for(data2 in roominfo){
                        SingleTonData.clubdata.add(data2.toObject(ClubData::class.java))
                        SingleTonData.clubdata.sortByDescending { it.positionx }
                    }
                }
                ClearData()
            }catch (e:Exception){
                Log.e(ContentValues.TAG,"Firebase Error : ${e.message}")
            }
        }
    }
    private fun initViewPager2() {
        binding.adviewpager.apply {
            viewPagerAdapter = ViewPagerAdapter()
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    isRunning=true
                    binding.bannertxtnumber.text = "${position+1}"
                    viewModel.setCurrentPosition(position)
                }
            })
        }
    }


    private fun subscribeObservers() {
        viewModel.useraddress.observe(this, Observer { userad->
            binding.homeUserAddress.text=userad
        })
        viewModel.bannerItemList.observe(this, Observer { bannerItemList ->
            viewPagerAdapter.submitList(bannerItemList)
        })
        viewModel.currentPosition.observe(this, Observer { currentPosition ->
            binding.adviewpager.currentItem = currentPosition
        })
    }
    private fun autoScrollViewPager() {
        lifecycleScope.launch {
            whenResumed {
                while (isRunning) {
                    delay(3000)
                    viewModel.getcurrentPosition()?.let {
                        viewModel.setCurrentPosition((it.plus(1)) % 4)
                    }
                }
            }
        }
    }
    private fun RoadData(){
        binding.mainhomeprogressbar.visibility= View.VISIBLE
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){
        binding.mainhomeprogressbar.visibility=View.GONE
        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        println("다ㅣ시시작")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10){
            viewModel.setuseraddress(SingleTonData.userInfo?.address!!)
            lifecycleScope.launch(Dispatchers.IO){
                SingleTonData.clubdata.clear()
                for(data in hobbydata){
                    val roominfo=db.collection("meeting_room").whereEqualTo("category",data).whereEqualTo("address",SingleTonData.userInfo?.address!!).get().await()
                    for(data2 in roominfo){
                        SingleTonData.clubdata.add(data2.toObject(ClubData::class.java))
                        SingleTonData.clubdata.sortByDescending { it.positionx }
                    }
                }
            }
        }
    }
}