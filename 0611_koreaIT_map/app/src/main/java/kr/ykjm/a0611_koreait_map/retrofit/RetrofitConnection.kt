package kr.ykjm.a0611_koreait_map.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConnection {
    // 객체 하나 생성 싱글턴 패턴 적용
    companion object {
        // API 서버의 주소가 BASE_URL
        private const val BASE_URL = "https://apis.data.go.kr/6270000/"
        private var INSTANCE: Retrofit? =null

        fun getInstace(): Retrofit {
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