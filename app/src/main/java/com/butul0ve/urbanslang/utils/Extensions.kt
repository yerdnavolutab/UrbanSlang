package com.butul0ve.urbanslang.utils

import android.content.Context
import android.support.v4.app.Fragment
import com.butul0ve.urbanslang.mvp.main.MainFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException


fun String.convertToFragment(): Fragment {

    return when(this) {
        MainFragment::class.java.simpleName -> MainFragment()
        else -> Fragment()
    }
}

@Throws(IOException::class)
fun readDictionaryFromAssets(context: Context): Map<String, List<String>> {
    val dictionary = HashMap<String, List<String>>()
    val alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray()

    alphabet.forEach {
        val reader = BufferedReader(InputStreamReader(context.assets.open("$it.txt")))

        val words = ArrayList<String>()

        reader.lineSequence().forEach {
            line -> words.add(line)
        }

        dictionary[it.toString()] = words

        reader.close()
    }

    return dictionary
}