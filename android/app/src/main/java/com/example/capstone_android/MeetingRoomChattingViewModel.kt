package com.example.capstone_android
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class ChattingData(val icon: Bitmap?, val nickname: String, val commentText:String,val timePosting:Long,
                   val writer_uid:String,val document_id: String)

class MeetingRoomChattingViewModel : ViewModel(){
    val itemsListData = MutableLiveData<ArrayList<ChattingData>>()
    val items = ArrayList<ChattingData>()

    var memberListArrStr =""

    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1
    var loginUserName="star1"

    fun addItem(item: ChattingData){
        items.add(item)
        itemsListData.value = items
    }
    fun updateItem(pos : Int ,item : ChattingData){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item:ChattingData){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
    fun findItem(document_id: String): ChattingData? {
        for (chatting in items){
            if (chatting.document_id == document_id){
                return chatting
            }
        }
        return null
    }
}