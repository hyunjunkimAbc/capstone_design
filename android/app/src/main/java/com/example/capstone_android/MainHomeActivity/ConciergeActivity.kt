package com.example.capstone_android

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.ActivityConciergeBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone_android.MainHomeActivity.BannerViewModel
import com.example.capstone_android.MainHomeActivity.ViewPagerAdapter
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.BannerItem
import com.example.capstone_android.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConciergeActivity : AppCompatActivity() {
        private lateinit var binding: ActivityConciergeBinding
        private lateinit var viewPagerAdapter: ViewPagerAdapter
        private lateinit var viewModel: BannerViewModel
        private lateinit var hobbydata:ArrayList<String>
        var address:String?=null
        private var isRunning = true
        lateinit var db : FirebaseFirestore
        @SuppressLint("SimpleDateFormat")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding=ActivityConciergeBinding.inflate(layoutInflater)
            db= Firebase.firestore
            setContentView(binding.root)
            binding.mainhomeprogressbar.visibility= View.GONE
            binding.homeUserAddress.setOnClickListener{
                val intent=Intent(this,AddressActivity::class.java)
                intent.putExtra("key","createuser")
                startActivityForResult(intent,10)
            }
            binding.Mainmeetingbutton.setOnClickListener{
                var intent= Intent(this, HomeActivity::class.java)
                intent.putExtra("openkey","periodic")
                startActivity(intent)
            }
            binding.Lightningmeetingbutton.setOnClickListener{
                var intent= Intent(this, HomeActivity::class.java)
                intent.putExtra("openkey","light")
                startActivity(intent)
            }
            binding.locationbutton.setOnClickListener{
                var intent= Intent(this, HomeActivity::class.java)
                intent.putExtra("openkey","place")
                startActivity(intent)
            }
            binding.Competitionbutton.setOnClickListener{
                var intent= Intent(this, HomeActivity::class.java)
                intent.putExtra("openkey","competition")
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
            viewModel.setuseraddress(SingleTonData.userInfo?.address!!)
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
            binding.mainhomeprogressbar.visibility= View.GONE
            this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        override fun onPause() {
            super.onPause()
            isRunning = false
        }

        override fun onResume() {
            super.onResume()
            isRunning = true
        }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if(requestCode==10){
                viewModel.setuseraddress(SingleTonData.userInfo?.address!!)

            }
        }
    }