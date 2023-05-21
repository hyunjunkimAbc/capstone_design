package com.example.capstone_android.data

class LightingMeetingRoomData : MeetingRoomData {
    //생성자 및 getter

    var member_list:ArrayList<String>?= arrayListOf<String>()
    var start_time:String=""
    var end_time :String =""
    var max:String=""
    var date :String =""
    var addressname:String = ""
    constructor():super()
    init {
        max =""
        start_time =""
        end_time =""
        date =""
        addressname = ""
        member_list= arrayListOf<String>()
    }
}