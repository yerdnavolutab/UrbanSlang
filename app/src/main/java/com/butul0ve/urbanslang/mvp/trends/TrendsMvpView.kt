package com.butul0ve.urbanslang.mvp.trends

import com.butul0ve.urbanslang.adapter.LetterAdapter
import com.butul0ve.urbanslang.adapter.WordAdapter
import com.butul0ve.urbanslang.mvp.MvpView

interface TrendsMvpView: MvpView {

    fun setLetterAdapter(letterAdapter: LetterAdapter)

    fun setWordAdapter(wordAdapter: WordAdapter)

    fun searchWord(word: String)

    fun saveLetter(letter: String)
}