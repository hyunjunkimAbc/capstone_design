package com.example.capstone_android.MeetingList

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone_android.SearchAddress.SharedPrefManager
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

class MeetingViewModel:ViewModel() {
    private val _MeetingItemList: MutableLiveData<ArrayList<ClubData>> = MutableLiveData()
    private val _LightMeetingItemList : MutableLiveData<ArrayList<lightData>> = MutableLiveData()
    private val _MeetingMapItemList : MutableLiveData<ArrayList<ClubData>> = MutableLiveData()
    private val _LightMeetingMapItemList : MutableLiveData<ArrayList<lightData>> = MutableLiveData()
    private val _PlaceItemList:MutableLiveData<ArrayList<PlaceData>> = MutableLiveData()
    private val _PlaceMapItemList:MutableLiveData<ArrayList<PlaceData>> = MutableLiveData()
    private val _CompetitionItemList:MutableLiveData<ArrayList<CompetitionData>> = MutableLiveData()
    private val _CompetitionMapItemList:MutableLiveData<ArrayList<CompetitionData>> = MutableLiveData()
    private var _MainSearchpopular: MutableLiveData<ArrayList<SearchPopularData>> = MutableLiveData()
    private var _MainSearchpopular2: MutableLiveData<ArrayList<SearchPopularData>> = MutableLiveData()
    private val _SearchMeetingItemList: MutableLiveData<ArrayList<ClubData>> = MutableLiveData()
    private val _SearchLightItemList: MutableLiveData<ArrayList<lightData>> = MutableLiveData()
    private val _SearchPlaceItemList: MutableLiveData<ArrayList<PlaceData>> = MutableLiveData()
    private val _SearchCompetitionItemList: MutableLiveData<ArrayList<CompetitionData>> = MutableLiveData()
    private val _MyMeetingRoomItemList: MutableLiveData<ArrayList<ClubData>> = MutableLiveData()
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

    val MyMeetingRoomItemList:MutableLiveData<ArrayList<ClubData>>
        get()=_MyMeetingRoomItemList

    val MainSearchpopular:MutableLiveData<ArrayList<SearchPopularData>>
        get()=_MainSearchpopular
    val MainSearchpopular2:MutableLiveData<ArrayList<SearchPopularData>>
        get()=_MainSearchpopular2
    val SearchMeetingItemList: MutableLiveData<ArrayList<ClubData>>
        get() = _SearchMeetingItemList
    val SearchLightItemList: MutableLiveData<ArrayList<lightData>>
        get()=_SearchLightItemList
    val SearchPlaceItemList: MutableLiveData<ArrayList<PlaceData>>
        get()=_SearchPlaceItemList
    val SearchCompetitionItemList: MutableLiveData<ArrayList<CompetitionData>>
        get()=_SearchCompetitionItemList
    fun loadMainSearchData(){
        SingleTonData.searchhpopulardata.clear()
        Log.d(TAG,"실시간 검색어 호출")
        val currentTime = Date()
        val timeLimit = Calendar.getInstance()
        timeLimit.add(Calendar.HOUR_OF_DAY, -2)
        viewModelScope.launch {
            db.collection("Search").whereGreaterThanOrEqualTo("timestamp",timeLimit.time).orderBy("timestamp",Query.Direction.DESCENDING).limit(10).get().addOnSuccessListener {
                    querySnapshot->
                for(document in querySnapshot.documents){
                    val keyword=document.id
                    val count=document.getLong("Count")
                    val data=SearchPopularData(count,keyword)
                    SingleTonData.searchhpopulardata.add(data)
                    SingleTonData.searchhpopulardata.sortByDescending { it.Count }
                }
                _MainSearchpopular.value=SingleTonData.searchhpopulardata
            }.addOnFailureListener{

            }
        }
    }
    fun loadMainPopData(){
        SingleTonData.searchhpopulardata2.clear()
        Log.d(TAG,"실시간 검색어 호출")
        viewModelScope.launch {
            db.collection("Search").orderBy("Count",Query.Direction.DESCENDING).limit(10).get().addOnSuccessListener {
                    querySnapshot->
                for(document in querySnapshot.documents){
                    val keyword=document.id
                    val count=document.getLong("Count")
                    val data=SearchPopularData(count,keyword)
                    SingleTonData.searchhpopulardata2.add(data)
                    SingleTonData.searchhpopulardata2.sortByDescending { it.Count }
                }
                _MainSearchpopular2.value=SingleTonData.searchhpopulardata2
            }.addOnFailureListener{

            }
        }
    }
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

    fun loadMyMeetingData(){
        SingleTonData.mymeetingroomdata.clear()
        viewModelScope.launch {
            val meetingdata=db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().await()
            val mdata=meetingdata.toObject(SignUpData::class.java)
            SingleTonData.userInfo?.meeting_room_id_list=mdata?.meeting_room_id_list
            val myroom=SingleTonData.userInfo?.meeting_room_id_list!!
            for(data in myroom){
                val roominfo=db.collection("periodic_meeting_room").document(data).get().await()
                roominfo.toObject(ClubData::class.java)?.let { SingleTonData.mymeetingroomdata.add(it) }
            }
            _MyMeetingRoomItemList.value=SingleTonData.mymeetingroomdata
        }
    }

}