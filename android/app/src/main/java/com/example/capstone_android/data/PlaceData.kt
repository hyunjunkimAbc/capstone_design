package com.example.capstone_android.data

data class PlaceData (var num_of_positive:Int?=0,
                      var title:String?=null,
                      var max:String?=null,
                      var info_text:String?=null,
                      var writer_uid:String?=null,
                      var address:String?=null,
                      var category:String?=null,
                      var upload_time:Long? = null,
                      var posting_id_list:ArrayList<String>?= ArrayList<String>(),
                      var chatting_id_list:ArrayList<String>?=ArrayList<String>(),
                      var positionx:Double?=null,
                      var positiony:Double?=null,
                      var reservation_id_list:ArrayList<String>?=ArrayList<String>(),
                      var Uid:String?=null,
                      var imageUrl: String? = null,
                      var addressdetail : String?=null)