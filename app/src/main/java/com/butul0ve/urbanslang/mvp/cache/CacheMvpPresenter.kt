package com.butul0ve.urbanslang.mvp.cache

import com.butul0ve.urbanslang.mvp.MvpPresenter

interface CacheMvpPresenter<V: CacheMvpView>: MvpPresenter<V> {

    fun loadAllCachedDefinitions()

    fun filterCachedDefinitions(query: String)
}