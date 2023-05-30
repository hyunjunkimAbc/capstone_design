package com.example.capstone_android.data

data class lightData(var title:String?=null,
                     var max:String?=null,
                     var info_text:String?=null,
                     var writer_uid:String?=null,
                     var member_list:ArrayList<String> ?= ArrayList<String>(),
                     var address:String?=null,
                     var addressname:String?=null,
                     var category:String?=null,
                     var upload_time: Long? = null,
                     var posting_id_list:ArrayList<String>?= ArrayList<String>(),
                     var chatting_id_list:ArrayList<String>?=ArrayList<String>(),
                     var date:String?=null,
                     var start_time:String?=null,
                     var end_time:String?=null,
                     var start_time2:String?=null,
                     var end_time2:String?=null,
                     var positionx:Double?=null,
                     var positiony:Double?=null,
                     var uid:String?=null,
                     var imageUrl: String? = null)