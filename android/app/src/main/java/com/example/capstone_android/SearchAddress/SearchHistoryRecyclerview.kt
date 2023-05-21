package com.example.capstone_android.SearchAddress

interface SearchHistoryRecyclerview {
    //검색 아이템 삭제버튼 클릭
    fun onSearchItemDeleteClicked(position:Int)

    //검색버튼 클릭
    fun onSearchItemClicked(position: Int)

}