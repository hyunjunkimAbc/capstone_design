package com.example.capstone_android

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
data class ReservationData(
    var reserDocument: String?,
    var placeImage: Bitmap?,
    var placeName: String = "",
    var reservatorName : String = "",
    var requestDate:String? = null,
    var startReservedSchedule:String?=null,
    var endReservedSchedule:String?=null,
    var numOfPeople:Int=0,
    var okCheck: Boolean = false,
    var requestToday:String? = null,
    var requestTime: Long? = null
)

class ReservationViewModel : ViewModel() {
    val itemsListData = MutableLiveData<ArrayList<ReservationData>>()
    val items = ArrayList<ReservationData>()

    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1

    fun addItem(item: ReservationData){
        items.add(item);
        itemsListData.value = items
    }
    fun updateItem(pos : Int ,item : ReservationData){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item:ReservationData){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
}