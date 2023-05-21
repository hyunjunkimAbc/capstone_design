package com.example.capstone_android.MainHomeActivity

import android.view.View
import com.example.capstone_android.data.BannerItem

interface Interaction: View.OnClickListener {
    fun onBannerItemClicked(bannerItem: BannerItem)
}