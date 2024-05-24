package kr.jeongmo.a0524__map_project.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LibraryService {
    // api키 / 데이터타입 / 서비스명 / 페이징 시작 번호 / 페이징 끝 번호
    @GET("{apiKey}/json/SeoulPublicLibraryInfo/1/200/")
    fun getLibrary(@Path("apiKey") key:String): Call<LibraryResponse>
}