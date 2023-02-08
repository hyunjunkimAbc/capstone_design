package com.example.capstone_android
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
data class Member(val icon: Bitmap?, val nickname: String, val profileMessage:String,val uid:String,val editTime :Long);

class MeetingRoomInfoViewModel : ViewModel() {
    val itemsListData = MutableLiveData<ArrayList<Member>>()
    val items = ArrayList<Member>()

    var memberListArrStr =""


    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1
    var loginUserName="star1"

    fun addItem(item: Member){
        items.add(item);
        itemsListData.value = items;
    }
    fun updateItem(pos : Int ,item : Member){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item:Member){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
}
