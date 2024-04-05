package com.butul0ve.urbanslang.network

import com.butul0ve.urbanslang.bean.BaseResponse

interface NetworkClient {

    suspend fun getRandomDefinition(): BaseResponse

    suspend fun getDefinition(query: String): BaseResponse
}