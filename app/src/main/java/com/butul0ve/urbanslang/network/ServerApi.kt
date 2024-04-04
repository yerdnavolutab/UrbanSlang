package com.butul0ve.urbanslang.network

import com.butul0ve.urbanslang.bean.BaseResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {

    @GET("/v0/define")
    fun getDefine(@Query("term") term: String): Single<BaseResponse>

    @GET("/v0/random")
    fun getRandomDefine(): Single<BaseResponse>
}