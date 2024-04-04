package com.butul0ve.urbanslang.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {

    fun getInstance(context: Context): SharedPreferences = context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )

    fun putBoolean(
        context: Context,
        key: String,
        value: Boolean
    ) {
        getInstance(context).edit().apply {
            putBoolean(key, value)
        }.apply()
    }
}