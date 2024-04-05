package com.butul0ve.urbanslang.network

import com.butul0ve.urbanslang.bean.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/v0/define")
    suspend fun getDefinition(@Query("term") term: String): BaseResponse

    @GET("/v0/random")
    suspend fun getRandomDefinition(): BaseResponse
}