package com.butul0ve.urbanslang.network

import com.butul0ve.urbanslang.bean.BaseResponse
import io.reactivex.Single

class AppNetworkHelper(private val api: ServerApi): NetworkHelper {

    override fun getData(query: String): Single<BaseResponse> {
        return if (query.isEmpty()) {
            api.getRandomDefine()
        } else {
            api.getDefine(query)
        }
    }
}