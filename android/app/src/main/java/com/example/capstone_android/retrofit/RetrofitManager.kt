package com.example.capstone_android.retrofit

import com.example.capstone_android.data.ResultSearchKeyword
import retrofit2.Retrofit
import com.example.capstone_android.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    companion object{
        val instance=RetrofitManager()
    }
    //retrofit interface 가져오기
    private var getRetrofit: ApiInterface? =RetrofitClient.getClient(ApiInfo.BASE_URL)?.create(ApiInterface::class.java)

    //POI 검색 api 호출
    fun searchPOI(searchpoi:String?,completion:(RESPONSESTATE,searchResult: ResultSearchKeyword?)->Unit){
        val term=searchpoi.let{
            it
        }?:""
       val call=getRetrofit?.getSearchResult(searchKeyword = term, appKey = ApiInfo.API_KEY).let{
           it
       }?:return

        call.enqueue(object:retrofit2.Callback<ResultSearchKeyword>{
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
               println("Retrofit 성공")
                completion(RESPONSESTATE.OKAY,response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                println("Retrofit 실패")
            }

        })
    }
}