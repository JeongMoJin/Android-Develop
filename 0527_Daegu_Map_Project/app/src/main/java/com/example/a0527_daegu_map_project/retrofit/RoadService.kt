package com.example.a0527_daegu_map_project.retrofit

import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoadService {
    @GET("dgInParkwalk/getDgWalkParkList")
    fun getRoad(@Query("serviceKey") apiKey: String,
                @Query("pageNo") pageNo:Int,
                @Query("numOfRows") numOfRows:Int,
                @Query("type") type: String,
                @Query("lat") lat: Double,
                @Query("lot") lot: Double,
                @Query("radius") radius:Int
                ):Call<RoadResponse>
}