package com.example.a0527_daegu_map_project.newRetrofit

data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)