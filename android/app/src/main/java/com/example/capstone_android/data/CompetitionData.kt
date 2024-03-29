package com.example.capstone_android.data

data class CompetitionData (var num_of_positive:Int?=0,
                           var title:String?=null,
                            var max:String?=null,
                            var info_text:String?=null,
                            var writer_uid:String?=null,
                            var member_list:ArrayList<String>?= ArrayList<String>(),
                            var address:String?=null,
                            var category:String?=null,
                            var upload_time:Long? = null,
                            var posting_id_list:ArrayList<String>?= ArrayList<String>(),
                            var chatting_id_list:ArrayList<String>?= ArrayList<String>(),
                            var start_time:String?=null,
                            var end_time:String?=null,
                            var location:String?=null,
                            var positionx:Double?=null,
                            var positiony:Double?=null,
                            var Uid:String?=null,
                            var imageUrl: String? = null)