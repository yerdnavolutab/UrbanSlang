package com.butul0ve.urbanslang.mvp.main

import com.butul0ve.urbanslang.mvp.MvpPresenter

interface MainMvpPresenter<V : MainMvpView> :
    MvpPresenter<V> {

    fun onViewInitialized()

    fun getData(query: String)
}