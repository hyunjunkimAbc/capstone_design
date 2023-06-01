package com.example.capstone_android.Alarm

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.CompetitionData
import com.example.capstone_android.data.SearchPopularData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

data class Alarmdata( val imageUrl:String, val isRead:Boolean,val message:String, val timestamp: String )

class AlarmViewModel: ViewModel() {
    val db= Firebase.firestore
    private val _AlarmItem: MutableLiveData<ArrayList<Alarmdata>> = MutableLiveData()
    val AlarmItem: MutableLiveData<ArrayList<Alarmdata>>
        get() = _AlarmItem

    fun loadalarm(){
        SingleTonData.Alarmdata.clear()
        Log.d(ContentValues.TAG,"실시간 검색어 호출")
        viewModelScope.launch {
            val alarminfo = db.collection("userAlarm").whereEqualTo("UserUid", "${Firebase.auth.uid}").get().await()
            for(data2 in alarminfo){
                Log.d(TAG,Firebase.auth.uid!!)
                val image=data2["imageUrl"].toString()
                val isread=data2["isRead"]
                val msg=data2["message"]
                val time=data2["timestamp"]
               val item=Alarmdata(image, isread as Boolean, msg as String, time as String)
                SingleTonData.Alarmdata.add(item)
                Log.d(TAG,SingleTonData.Alarmdata.toString())
               SingleTonData.Alarmdata.sortByDescending { it.timestamp }
                    }
            _AlarmItem.value= SingleTonData.Alarmdata
                 }

            }



}