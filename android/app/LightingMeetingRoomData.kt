package com.example.capstone_android.data

class LightingMeetingRoomData : MeetingRoomData {
    //생성자 및 getter
    var positionx = 0.1
    var positiony = 0.1
    var member_list:ArrayList<String>?= arrayListOf<String>()
    var start_time:Long=0
    var end_time :Long =0
    var max:String=""
    constructor():super()
    init {
        max =""
        start_time =0
        end_time =0
        positionx = 0.1
        positiony =0.1
        member_list= arrayListOf<String>()
    }
}