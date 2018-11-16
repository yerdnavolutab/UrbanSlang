package com.butul0ve.urbanslang.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
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
fun readDictionaryFromAssets(context: Context): MutableMap<String, List<String>> {
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

    return dictionary.toMutableMap()
}

fun View.hideKeyboard(context: Context) {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    manager?.hideSoftInputFromWindow(this.windowToken, 0)
}