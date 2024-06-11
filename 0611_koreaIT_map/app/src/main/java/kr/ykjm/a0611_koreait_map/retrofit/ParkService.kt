package kr.ykjm.a0611_koreait_map.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkService {
    @GET("dgInParkexeequip/getDgExeequipParkList")
    fun getPark(@Query("serviceKey") apiKey: String,
                @Query("pageNo") pageNo:Int,
                @Query("numOfRows") numOfRows:Int,
                @Query("type") type: String,
                @Query("lat") lat: Double,
                @Query("lot") lot: Double,
                @Query("radius") radius:Int
    ): Call<ParkRespone>
}