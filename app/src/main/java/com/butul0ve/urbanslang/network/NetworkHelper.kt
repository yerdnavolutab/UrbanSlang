package com.butul0ve.urbanslang.network

import com.butul0ve.urbanslang.bean.BaseResponse
import io.reactivex.Single

interface NetworkHelper {

    fun getData(query: String = ""): Single<BaseResponse>
}