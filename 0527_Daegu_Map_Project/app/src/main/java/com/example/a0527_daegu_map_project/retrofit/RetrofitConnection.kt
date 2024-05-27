package com.example.a0527_daegu_map_project.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConnection {
    // 객체를 하나만 생성하는 싱글턴 패턴을 적용.
    companion object {
        // API 서버의 주소가 BASE_URL이 됨.
        private const val BASE_URL = "https://apis.data.go.kr/6270000/"
        private var INSTANCE: Retrofit? = null

        fun getInstance(): Retrofit {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE!!
        }
    }
}