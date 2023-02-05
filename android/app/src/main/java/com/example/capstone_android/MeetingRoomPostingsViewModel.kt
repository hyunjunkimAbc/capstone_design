package com.example.capstone_android


import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Posting(val icon: Bitmap?, val nickname: String, val postingText:String,val timePosting:Long,val document_id:String,
                   val writer_uid:String)

class MeetingRoomPostingsViewModel : ViewModel(){
    val itemsListData = MutableLiveData<ArrayList<Posting>>()
    val items = ArrayList<Posting>()

    var memberListArrStr =""

    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1
    var loginUserName="star1"

    fun addItem(item: Posting){
        items.add(item)
        itemsListData.value = items
    }
    fun updateItem(pos : Int ,item : Posting){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item:Posting){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
    fun findItem(document_id: String): Posting? {
        for (posting in items){
            if (posting.document_id == document_id){
                return posting
            }
        }
        return null
    }
}