package com.example.capstone_android


import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Comment(val icon: Bitmap?, val nickname: String, val commentText:String,val timePosting:Long,
                   val writer_uid:String,val document_id: String)

class ShowPostingViewModel : ViewModel(){
    val itemsListData = MutableLiveData<ArrayList<Comment>>()
    val items = ArrayList<Comment>()

    var memberListArrStr =""

    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1
    var loginUserName="star1"

    fun addItem(item: Comment){
        items.add(item)
        itemsListData.value = items
    }
    fun updateItem(pos : Int ,item : Comment){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item:Comment){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
    fun findItem(document_id: String): Comment? {
        for (comment in items){
            if (comment.document_id == document_id){
                return comment
            }
        }
        return null
    }
}