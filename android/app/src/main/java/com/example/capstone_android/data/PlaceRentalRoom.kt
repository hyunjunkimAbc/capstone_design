package com.example.capstone_android.data

class PlaceRentalRoom :MeetingRoomData{
    var imageUrl: String
    var max : String
    var Uid : String

    var reservation_uid_list :ArrayList<String>? = arrayListOf<String>()
    var addressdetail =""
    constructor():super()
    init {
        imageUrl=""
        max = ""
        Uid = ""

        reservation_uid_list = arrayListOf<String>()
    }
}