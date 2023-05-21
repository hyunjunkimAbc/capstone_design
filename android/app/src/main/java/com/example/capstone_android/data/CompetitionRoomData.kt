package com.example.capstone_android.data

class CompetitionRoomData :MeetingRoomData {

    var member_list:ArrayList<String>?= arrayListOf<String>()
    var start_time =""
    var end_time =""
    var location =""
    constructor():super()
    init {

        member_list= arrayListOf<String>()
        start_time =""
        end_time =""
        location =""
    }
}