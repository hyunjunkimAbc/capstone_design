package com.example.capstone_android.data

data class ReservationRequestData (
    var placeName: String = "",
    var placeUid:String = "",
    var reservatorUid : String = "",
    var reservatorName : String = "",
    var requestDate:String? = null,
    var startReservedSchedule:String?=null,
    var endReservedSchedule:String?=null,
    var numOfPeople:Int=0,
    var okCheck: Boolean = false,
    var requestToday:String? = null,
    var requestTime: Long? = null
)