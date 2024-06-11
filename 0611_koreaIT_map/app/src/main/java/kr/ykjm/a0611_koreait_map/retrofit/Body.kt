package kr.ykjm.a0611_koreait_map.retrofit

data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)