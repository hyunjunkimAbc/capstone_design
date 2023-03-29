import com.example.capstone_android.data.ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {
    @GET("tmap/pois?version=1&format=json&callback=result&count=20&radius=33&centerLat=37.58178633&centerLon=127.01035102")
    fun getSearchResult(
        @Query("searchKeyword") searchKeyword: String?,
        @Query("appKey") appKey: String?
    ): Call<ResultSearchKeyword>
}