package com.example.capstone_android.data

data class ResultSearchKeyword(
    val searchPoiInfo:searchPoiInfo,
)

data class searchPoiInfo(
    var totalCount:String,
    var count:String,
    var page:String,
    var pois:poi
)
data class poi(
    var poi:List<data>
)
data class data(
    var name:String,
    var noorLat:Double,
    var noorLon:Double,
    var upperAddrName:String,
    var middleAddrName:String,
   var lowerAddrName:String,
    var detailAddrName:String,
    var radius:String?=null
)


