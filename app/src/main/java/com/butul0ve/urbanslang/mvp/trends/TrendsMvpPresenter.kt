package com.butul0ve.urbanslang.mvp.trends

import com.butul0ve.urbanslang.mvp.MvpPresenter

interface TrendsMvpPresenter<V: TrendsMvpView>: MvpPresenter<V> {

    fun showWordsByLetter(letter: String = "a")
}