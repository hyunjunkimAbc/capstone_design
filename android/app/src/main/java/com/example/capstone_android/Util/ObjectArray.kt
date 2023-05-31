package com.example.capstone_android.Util

import com.example.capstone_android.data.*

object SingleTonData{
    val clubdata :ArrayList<ClubData> = arrayListOf()
    val lightdata:ArrayList<lightData> = arrayListOf()
    val clubmapdata : ArrayList<ClubData> = arrayListOf()
    val lightmapdata : ArrayList<lightData> = arrayListOf()
    val placedata:ArrayList<PlaceData> = arrayListOf()
    val placemapdata:ArrayList<PlaceData> = arrayListOf()
    val competitiondata:ArrayList<CompetitionData> = arrayListOf()
    val competitionmapdata:ArrayList<CompetitionData> = arrayListOf()
    val searchhpopulardata : ArrayList<SearchPopularData> = arrayListOf()
    val searchhpopulardata2 : ArrayList<SearchPopularData> = arrayListOf()
    var userInfo :SignUpData ?=null
    val mymeetingroomdata : ArrayList<ClubData> = arrayListOf()
}

object MainMenuId{
    val periodic :String ="periodic"
    val light : String="light"
    val place :String="place"
    val competition:String="competition"
}

object LightTimeCheck{
    var timecheck :Int = 0
    var index :Int=0
}