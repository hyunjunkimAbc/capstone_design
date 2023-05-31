package com.example.capstone_android

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
// 대회 아이콘, 대회명, 대회설명, 작성시간, 탐색용 id
data class Creation(val icon: Bitmap?, val title: String, val infoText:String,val uploadTime:Long,val document_id:String)

class MyCreateCompetitionViewModel : ViewModel(){

    val itemsListData = MutableLiveData<ArrayList<Creation>>()
    val items = ArrayList<Creation>()

    var memberListArrStr =""

    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1

    fun addItem(item: Creation){
        items.add(item)
        itemsListData.value = items
    }
    fun updateItem(pos : Int ,item : Creation){
        items[pos] = item
        itemsListData.value = items
    }
    fun deleteItem(item : Creation){
        //items.removeAt(pos)
        items.remove(item)
        itemsListData.value = items
    }
    fun findItem(document_id: String): Creation? {
        for (Creation in items){
            if (Creation.document_id == document_id){
                return Creation
            }
        }
        return null
    }

}