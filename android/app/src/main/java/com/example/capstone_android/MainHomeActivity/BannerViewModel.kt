package com.example.capstone_android.MainHomeActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstone_android.data.BannerItem
import com.example.capstone_android.data.ClubData

class BannerViewModel : ViewModel() {
    private val _bannerItemList: MutableLiveData<List<BannerItem>> = MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()
    private val _useraddress: MutableLiveData<String> =MutableLiveData()
    private val _mainhomemeetingList: MutableLiveData<List<ClubData>> = MutableLiveData()
    val bannerItemList: LiveData<List<BannerItem>>
        get() = _bannerItemList

    val currentPosition: LiveData<Int>
        get() = _currentPosition

    val useraddress:LiveData<String>
        get()=_useraddress

    val mainhomemeetingList: LiveData<List<ClubData>>
        get() = _mainhomemeetingList
    init{
        _currentPosition.value=0
    }
    fun setuseraddress(address:String){
        _useraddress.value=address
    }
    fun setBannerItems(list: List<BannerItem>){
        _bannerItemList.value = list
    }
    fun setCurrentPosition(position: Int){
        _currentPosition.value = position
    }
    fun setmeetingList(list: List<ClubData>){
        _mainhomemeetingList.value = list
    }
    fun getcurrentPosition() = currentPosition.value
}