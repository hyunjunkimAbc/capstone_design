package com.example.capstone_android.data

class CompetitionRoomData :MeetingRoomData {
    var positionx = 0.1
    var positiony = 0.1
    var member_list:ArrayList<String>?= arrayListOf<String>()
    var start_time =""
    var end_time =""
    constructor():super()
    init {
        positionx = 0.1
        positiony = 0.1
        member_list= arrayListOf<String>()
        start_time =""
        end_time =""
    }
}