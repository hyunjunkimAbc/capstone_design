package com.example.capstone_android.retrofit

import com.example.capstone_android.data.ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET(ApiInfo.SearchPOI)
    fun getSearchResult(
        @Query("searchKeyword") searchKeyword: String?,
        @Query("appKey") appKey: String?
    ): Call<ResultSearchKeyword>
}