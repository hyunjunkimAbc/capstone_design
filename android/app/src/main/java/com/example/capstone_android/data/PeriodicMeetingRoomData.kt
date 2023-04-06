package com.example.capstone_android.data

class PeriodicMeetingRoomData:MeetingRoomData {
    var start_time:Long=0
    var end_time :Long =0
    var positionx = 0.1
    var positiony = 0.1
    var member_list:ArrayList<String>?= arrayListOf<String>()
    constructor():super()
    init {
        start_time=0
        end_time =0
        positionx = 0.1
        positiony = 0.1
        member_list= arrayListOf<String>()
    }
}