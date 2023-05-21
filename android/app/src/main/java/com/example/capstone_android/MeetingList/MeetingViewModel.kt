package com.example.capstone_android.MeetingList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.CompetitionData
import com.example.capstone_android.data.PlaceData
import com.example.capstone_android.data.lightData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MeetingViewModel:ViewModel() {
    private val _MeetingItemList: MutableLiveData<ArrayList<ClubData>> = MutableLiveData()
    private val _LightMeetingItemList : MutableLiveData<ArrayList<lightData>> = MutableLiveData()
    private val _MeetingMapItemList : MutableLiveData<ArrayList<ClubData>> = MutableLiveData()
    private val _LightMeetingMapItemList : MutableLiveData<ArrayList<lightData>> = MutableLiveData()
    private val _PlaceItemList:MutableLiveData<ArrayList<PlaceData>> = MutableLiveData()
    private val _PlaceMapItemList:MutableLiveData<ArrayList<PlaceData>> = MutableLiveData()
    private val _CompetitionItemList:MutableLiveData<ArrayList<CompetitionData>> = MutableLiveData()
    private val _CompetitionMapItemList:MutableLiveData<ArrayList<CompetitionData>> = MutableLiveData()
    val db= Firebase.firestore
    val MeetingItemList: MutableLiveData<ArrayList<ClubData>>
        get() = _MeetingItemList

    val LightMeetingItemList : MutableLiveData<ArrayList<lightData>>
        get()=_LightMeetingItemList

    val MeetingMapItemList:MutableLiveData<ArrayList<ClubData>>
         get()=_MeetingMapItemList

    val LightMeetingMapItemList:MutableLiveData<ArrayList<lightData>>
        get()=_LightMeetingMapItemList

    val PlaceItemList:MutableLiveData<ArrayList<PlaceData>>
        get()=_PlaceItemList

    val PlaceMapItemList:MutableLiveData<ArrayList<PlaceData>>
        get()=_PlaceMapItemList

    val CompetitionItemList:MutableLiveData<ArrayList<CompetitionData>>
        get()=_CompetitionItemList

    val CompetitionMapItemList:MutableLiveData<ArrayList<CompetitionData>>
        get()=_CompetitionMapItemList
    fun loadMeetingData(){
        SingleTonData.clubdata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("periodic_meeting_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                    SingleTonData.clubdata.add(data2.toObject(ClubData::class.java))
                    SingleTonData.clubdata.sortByDescending { it.positionx }
                }
            }
            _MeetingItemList.value=SingleTonData.clubdata
        }
    }

    fun loadLightMeetingData(){
        SingleTonData.lightdata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("lighting_meeting_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                  SingleTonData.lightdata.add(data2.toObject(lightData::class.java))
                    SingleTonData.lightdata.sortBy { it.date }
                }
            }
            _LightMeetingItemList.value=SingleTonData.lightdata
        }
    }

    fun loadMeetingMapData(){
        SingleTonData.clubmapdata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("periodic_meeting_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                    SingleTonData.clubmapdata.add(data2.toObject(ClubData::class.java))
                    SingleTonData.clubmapdata.sortByDescending { it.positionx }
                }
            }
            _MeetingMapItemList.value=SingleTonData.clubmapdata
        }
    }
    fun loadLightMeetingMapData(){
        SingleTonData.lightmapdata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("lighting_meeting_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                    SingleTonData.lightmapdata.add(data2.toObject(lightData::class.java))
                    SingleTonData.lightmapdata.sortByDescending { it.positionx }
                }
            }
            _LightMeetingMapItemList.value=SingleTonData.lightmapdata
        }
    }
    fun loadPlaceData(){
        SingleTonData.placedata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("place_rental_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                    SingleTonData.placedata.add(data2.toObject(PlaceData::class.java))
                    SingleTonData.placedata.sortBy { it.num_of_positive.toString() }
                }
            }
            _PlaceItemList.value=SingleTonData.placedata
        }
    }
    fun loadPlaceMapData(){
        SingleTonData.placemapdata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("place_rental_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                    SingleTonData.placemapdata.add(data2.toObject(PlaceData::class.java))
                    SingleTonData.placemapdata.sortBy { it.num_of_positive.toString() }
                }
            }
            _PlaceMapItemList.value=SingleTonData.placemapdata
        }
    }

    fun loadCompetitionData(){
        SingleTonData.competitiondata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("competition_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                    SingleTonData.competitiondata.add(data2.toObject(CompetitionData::class.java))
                    SingleTonData.competitiondata.sortByDescending { it.num_of_positive.toString() }
                }
            }
            _CompetitionItemList.value=SingleTonData.competitiondata
        }
    }
    fun loadCompetitionMapData(){
        SingleTonData.competitionmapdata.clear()
        viewModelScope.launch {
            val address= SingleTonData.userInfo?.address
            val hobbydata= SingleTonData.userInfo?.interest_array!!
            for(data in hobbydata) {
                val roominfo = db.collection("competition_room").whereEqualTo("category", data).whereEqualTo("address", address).get().await()
                for(data2 in roominfo){
                    SingleTonData.competitionmapdata.add(data2.toObject(CompetitionData::class.java))
                    SingleTonData.competitionmapdata.sortByDescending { it.num_of_positive.toString() }
                }
            }
            _CompetitionMapItemList.value=SingleTonData.competitionmapdata
        }
    }

}