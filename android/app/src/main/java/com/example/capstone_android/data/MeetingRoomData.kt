package com.example.capstone_android.data

open class MeetingRoomData {
    var title:String=""
    var info_text:String=""
    var writer_uid:String=""


    var category: String=""
    var upload_time:Long=0
    var posting_id_list:ArrayList<String>?=  arrayListOf<String>()
    var chatting_id_list :ArrayList<String>?= arrayListOf<String>()



    var address= ""
    var positionx = 0.1
    var positiony = 0.1
    init {
        title=""
        address =""
        info_text=""
        writer_uid=""
        category=""
        upload_time=0
        posting_id_list= arrayListOf<String>()
        chatting_id_list= arrayListOf<String>()
    }
}