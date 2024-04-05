package com.butul0ve.urbanslang.network

import com.butul0ve.urbanslang.bean.BaseResponse

class NetworkClientImpl(private val api: Api) : NetworkClient {
    override suspend fun getRandomDefinition(): BaseResponse = api.getRandomDefinition()

    override suspend fun getDefinition(query: String): BaseResponse = api.getDefinition(query)
}