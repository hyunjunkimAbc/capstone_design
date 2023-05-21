package com.example.capstone_android.SearchAddress

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.capstone_android.Util.App
import com.example.capstone_android.data.SearchData
import com.google.gson.Gson
import jxl.write.BoldStyle

object SharedPrefManager {
    private const val SHARED_SEARCH_HISTORY="shared_search_history"
    private const val KEY_SEARCH_HISTORY="key_search_history"

    private const val SHARED_SEARCH_HISTORY_MODE="shared_search_history_mode"
    private const val KEY_SEARCH_HISTORY_MODE="key_search_history_mode"

    //검색어 저장모드 설정
    fun setSearchHistoryMode(isActivated:Boolean){
        Log.d(ContentValues.TAG,"SharedPrefManager - SetSearchHistroyMode() called / isActivated: $isActivated")
        val shared= App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY_MODE, Context.MODE_PRIVATE)
        val editor=shared.edit()
        editor.putBoolean(KEY_SEARCH_HISTORY_MODE,isActivated)
        editor.apply()
    }
    //검색어 저장모드 확인하기
    fun checkSearchHistoryMode(): Boolean {
        val shared=App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY_MODE, Context.MODE_PRIVATE)
        val isSearchHistoryMode=shared.getBoolean(KEY_SEARCH_HISTORY_MODE,false)
        return isSearchHistoryMode
    }

    fun storeSearchHistoryList(searchHistoryList:MutableList<SearchData>){
        Log.d(ContentValues.TAG,"쉐어드 매니저 불림")
        val searchHistoryListString:String= Gson().toJson(searchHistoryList)
        Log.d(ContentValues.TAG,"${searchHistoryListString}")

        //쉐어드 가져오기
        val shared=App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

        //쉐어드 에디터 가져오기
        val editor=shared.edit()
        editor.putString(KEY_SEARCH_HISTORY,searchHistoryListString)
        editor.apply()
    }

    //검색목록 가져오기
    fun getSearchHistoryList():MutableList<SearchData>{
        val shared=App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)
        val storedSearchHistoryListString=shared.getString(KEY_SEARCH_HISTORY,"")!!

        var storedSearchHistoryList=ArrayList<SearchData>()
        if(storedSearchHistoryListString.isNotEmpty()){
            //저장된 문자열 -> 객체 배열로 변경
            storedSearchHistoryList=Gson().fromJson(storedSearchHistoryListString,Array<SearchData>::class.java).toMutableList() as ArrayList<SearchData>
        }
        return storedSearchHistoryList
    }

    //검색목록 지우기
    fun clearSearchHistoryList(){
        Log.d(ContentValues.TAG,"SharedPrefManager - clearSearchHistoryList() called / removeall")
        //쉐어드 가져오기
        val shared=App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

        //쉐어드 에디터 가져오기
        val editor=shared.edit()
        editor.clear()
        editor.apply()
    }
}