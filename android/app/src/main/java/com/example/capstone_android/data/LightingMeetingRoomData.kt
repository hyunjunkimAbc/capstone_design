package com.example.capstone_android.data

class LightingMeetingRoomData : MeetingRoomData {
    //생성자 및 getter
    var positionx = 0.1
    var positiony = 0.1
    var member_list:ArrayList<String>?= arrayListOf<String>()
    constructor():super()
    init {
        positionx = 0.1
        positiony =0.1
        member_list= arrayListOf<String>()
    }
}