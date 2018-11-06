package com.butul0ve.urbanslang.utils

import android.support.v4.app.Fragment
import com.butul0ve.urbanslang.mvp.main.MainFragment

fun String.convertToFragment(): Fragment {

    return when(this) {
        MainFragment::class.java.simpleName -> MainFragment()
        else -> Fragment()
    }
}