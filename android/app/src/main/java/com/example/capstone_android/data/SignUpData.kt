package com.example.capstone_android.data
import java.util.HashMap
data class SignUpData(var email:String?=null,
                      var nickname:String?=null,
                      var uid:String?=null,
                      var birthday:String?=null,
                      var timestamp: Long? = null,
                      var profile_message:String?=null,
                      var interest_array:ArrayList<String>?=ArrayList<String>(),
                      var meeting_room_id_list:ArrayList<String>?=ArrayList<String>(),
                      var lightmeeting_room_id_list: ArrayList<String>?=ArrayList<String>(),
                      var place_id_list: ArrayList<String>?=ArrayList<String>(),
                      var reservation_list:ArrayList<String>?=ArrayList<String>(),
                      var competition_id_list:ArrayList<String>?=ArrayList<String>(),
                      var edit_time:Long?=null,
                      var address:String?=null,
                        var photoUri:String?=null)