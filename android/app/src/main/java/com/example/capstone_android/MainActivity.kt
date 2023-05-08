
package com.example.capstone_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone_android.MainHomeActivity.BannerViewModel
import com.example.capstone_android.MainHomeActivity.Interaction
import com.example.capstone_android.MainHomeActivity.ViewPagerAdapter
import com.example.capstone_android.data.BannerItem
import com.example.capstone_android.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewModel: BannerViewModel
    private var isRunning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.homeUserAddress.setOnClickListener{

        }
        viewModel = ViewModelProvider(this)[BannerViewModel::class.java]
        viewModel.setBannerItems(
            listOf(BannerItem(R.drawable.banner1), BannerItem(R.drawable.banner2), BannerItem(R.drawable.banner3), BannerItem(R.drawable.banner4),))
        initViewPager2()
        subscribeObservers()
        autoScrollViewPager()
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
    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
    }
}
