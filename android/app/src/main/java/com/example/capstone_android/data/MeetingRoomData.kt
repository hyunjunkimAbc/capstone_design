package com.example.capstone_android.data

open class MeetingRoomData {
    var title:String=""
    var info_text:String=""
    var writer_uid:String=""

    var address:String=""
    var category: String=""
    var upload_time:Long=0
    var posting_id_list:ArrayList<String>?=  arrayListOf<String>()
    var chatting_id_list :ArrayList<String>?= arrayListOf<String>()
    init {
        title=""
        info_text=""
        writer_uid=""
        address=""
        category=""
        upload_time=0
        posting_id_list= arrayListOf<String>()
        chatting_id_list= arrayListOf<String>()
    }
}