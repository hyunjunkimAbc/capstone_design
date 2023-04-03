package com.example.capstone_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.data.ListLayout
import com.example.capstone_android.data.ResultSearchKeyword
import com.example.capstone_android.retrofit.RESPONSESTATE
import com.example.capstone_android.retrofit.RetrofitManager
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_searchtext.*
import kotlinx.android.synthetic.main.fragment_searchtext.view.*
import kotlinx.android.synthetic.main.searchaddressitem.view.*
import retrofit2.converter.gson.GsonConverterFactory


class SearchTextFragment:Fragment() {
    companion object {
        const val BASE_URL = "https://apis.openapi.sk.com/"
        const val API_KEY =  "VwYv1tFJtY1v9qhvVmkP92XdfO8UF8Kj3Hu83jRL" // REST API 키
    }
    private val listItems = arrayListOf<ListLayout>() // 리사이클러 뷰 아이템
    private val listAdapter = SearchTextAdapter(listItems) // 리사이클러 뷰 어댑터
    private var pageNumber = 1 // 검색 페이지 번호
    private var keyword = "" // 검색 키워드

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_searchtext,container,false)
        val searchrecyclerView:RecyclerView=view.search_recycler
        searchrecyclerView.layoutManager= LinearLayoutManager(activity)
        searchrecyclerView.adapter = listAdapter
        listAdapter.setItemClickListener(object: SearchTextAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
              println(listItems[position].noorLat)
                println(listItems[position].noorLon)
                /*
                val intent= Intent()

                val name=listItems[position].name
                val positiony=listItems[position].noorLat
                val positionx=listItems[position].noorLon

                intent.putExtra("address",address)
                intent.putExtra("name",name)
                intent.putExtra("disy",positiony)
                intent.putExtra("disx",positionx)
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
                println("정상종료")

                 */
                val text1=listItems[position].upperAddrName
                val text2=listItems[position].middleAddrNmae
                val address=text1.plus(" ").plus(text2)
                val bundle=Bundle()
                bundle.putString("address",address)
                bundle.putDouble("ydis",listItems[position].noorLon)
                bundle.putDouble("xdis",listItems[position].noorLat)
                bundle.putString("name",listItems[position].name)
                val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                val searchmap=SearchMapFragment()
                searchmap.arguments = bundle;//번들을 프래그먼트2로 보낼 준비
               transaction.replace(R.id.search_content, searchmap);
                transaction.commit();
            }
        })


        view.searchtext.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                println("검색햇어")
                println(query)
                keyword = query
                pageNumber = 1
                searchKeyword(keyword,API_KEY)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어가 변경되었을 때 이벤트 처리
                return false
            }
        })
        view.previous.setOnClickListener{
            pageNumber--
           // searchKeyword(keyword,pageNumber)
        }
        view.next.setOnClickListener{
            pageNumber++
            //searchKeyword(keyword, pageNumber)
        }

        return view
    }


    private fun searchKeyword(keyword: String, page: String) {
        /*
        val retrofit = Retrofit.Builder() // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ApiInterface::class.java) // 통신 인터페이스를 객체로 생성

        val call = api.getSearchResult(keyword,API_KEY) // 검색 조건 입력

// API 서버에 요청
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
        Log.d("Test", "Raw: ${response.raw()}")
    Log.d("Test", "Body: ${response.body()}")
               addItemsAndMarkers(response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
// 통신 실패
                Log.w("LocalSearch", "통신 실패: ${t.message}")
            }
        })
         */
        RetrofitManager.instance.searchPOI(searchpoi = keyword, completion = {responseState,response->
           when(responseState){
              RESPONSESTATE.OKAY->addItemsAndMarkers(response)
           }
        })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        if (!searchResult?.searchPoiInfo?.pois?.poi.isNullOrEmpty()) {
// 검색 결과 있음
            listItems.clear() // 리스트 초기화
            for (document in searchResult!!.searchPoiInfo?.pois?.poi) {
// 결과를 리사이클러 뷰에 추가
                val item = ListLayout(
                   document.name,
                    document.upperAddrName,
                    document.middleAddrName,
                    document.lowerAddrName,
                    document.detailAddrName,
                    document.noorLat,
                    document.noorLon
                )
                listItems.add(item)
                listAdapter.notifyDataSetChanged()
            }
        }
        else {
// 검색 결과 없음
            Toast.makeText(context, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

}