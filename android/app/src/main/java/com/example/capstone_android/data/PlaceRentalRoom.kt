package com.example.capstone_android.data

class PlaceRentalRoom :MeetingRoomData{
    var positionx = 0.1
    var positiony = 0.1
    var reservation_uid_list :ArrayList<String>? = arrayListOf<String>()
    constructor():super()
    init {
        positionx = 0.1
        positiony = 0.1
        reservation_uid_list = arrayListOf<String>()
    }
}