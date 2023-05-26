package com.example.capstone_android

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
// 대회 아이콘, 대회명, 대회설명, 작성시간, 탐색용 id
data class Apply(val icon: Bitmap?, val title: String, val infoText:String,val uploadTime:Long,val document_id:String)

class MyApplyCompetitionViewModel : ViewModel(){

    val itemsListData = MutableLiveData<ArrayList<Apply>>()
    val items = ArrayList<Apply>()

    var memberListArrStr =""

    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1

    fun addItem(item: Apply){
        items.add(item)
        itemsListData.value = items
    }
    fun updateItem(pos : Int ,item : Apply){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item : Apply){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
    fun findItem(document_id: String): Apply? {
        for (Apply in items){
            if (Apply.document_id == document_id){
                return Apply
            }
        }
        return null
    }

}