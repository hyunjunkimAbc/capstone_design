import com.example.capstone_android.data.ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {
    @GET("tmap/pois?version=1&format=json&callback=result&count=20")
    fun getSearchResult(
        @Query("searchKeyword") searchKeyword: String?,
        @Query("appKey") appKey: String?
    ): Call<ResultSearchKeyword>
}