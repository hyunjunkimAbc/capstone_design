package com.example.capstone_android.data

class PlaceRentalRoom :MeetingRoomData{
    var imageUrl: String
    var max : String
    var Uid : String
    var positionx = 0.1
    var positiony = 0.1
    var reservation_uid_list :ArrayList<String>? = arrayListOf<String>()
    var addressdetail : String
    constructor():super()
    init {
        imageUrl=""
        max = ""
        addressdetail=""
        Uid = ""
        positionx = 0.1
        positiony = 0.1
        reservation_uid_list = arrayListOf<String>()
    }
}