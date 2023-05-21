package com.example.capstone_android.data

class PeriodicMeetingRoomData:MeetingRoomData {


    var member_list:ArrayList<String>?= arrayListOf<String>()
    var max:String=""
    constructor():super()
    init {
        max=""
        member_list= arrayListOf<String>()
    }
}