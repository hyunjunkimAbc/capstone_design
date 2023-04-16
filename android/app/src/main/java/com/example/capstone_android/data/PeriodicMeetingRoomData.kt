package com.example.capstone_android.data

class PeriodicMeetingRoomData:MeetingRoomData {

    var positionx = 0.1
    var positiony = 0.1
    var member_list:ArrayList<String>?= arrayListOf<String>()
    var max:String=""
    constructor():super()
    init {
        max=""
        positionx = 0.1
        positiony = 0.1
        member_list= arrayListOf<String>()
    }
}