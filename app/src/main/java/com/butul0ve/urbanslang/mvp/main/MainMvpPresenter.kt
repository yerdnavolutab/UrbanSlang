package com.butul0ve.urbanslang.mvp.main

import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.MvpPresenter

interface MainMvpPresenter<V : MainMvpView> :
    MvpPresenter<V> {

    fun onFirstViewInitialized()

    fun onViewInitialized()

    fun getData(query: String)

    fun getDefinitions(): List<Definition>?
}