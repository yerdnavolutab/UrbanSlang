package com.butul0ve.urbanslang.bean

import com.google.gson.annotations.SerializedName

data class BaseResponse(@SerializedName("tags") val tags: List<String>,
                        @SerializedName("result_type") val resultType: String,
                        @SerializedName("list") val definitions: List<Definition>)