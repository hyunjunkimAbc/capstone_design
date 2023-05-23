package com.example.capstone_android.MainHomeActivity

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class MeetingRoom(val title: String,val document_id:String,val col_name:String,val info_text:String,val imageUrl :String,val context: Context,val category:String)
open class ConciergeViewModel : ViewModel(){
    val itemsListData = MutableLiveData<ArrayList<MeetingRoom>>()
    val items = ArrayList<MeetingRoom>()

    var memberListArrStr =""
    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1
    var loginUserName="star1"


    fun addItem(item: MeetingRoom){
        items.add(item)
        itemsListData.value = items
    }
    fun updateItem(pos : Int ,item : MeetingRoom){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item: MeetingRoom){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
}
class LightingViewModel : ConciergeViewModel(){

}
class PeriodicViewModel : ConciergeViewModel(){

}
class PlaceRentalViewModel : ConciergeViewModel(){

}
class CompetitionViewModel : ConciergeViewModel(){

}