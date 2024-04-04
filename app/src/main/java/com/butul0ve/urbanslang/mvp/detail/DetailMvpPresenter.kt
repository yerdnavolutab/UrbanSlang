package com.butul0ve.urbanslang.mvp.detail

import com.butul0ve.urbanslang.mvp.MvpPresenter

interface DetailMvpPresenter<V: DetailMvpView>: MvpPresenter<V> {

    fun loadDefinition(id: Long)

    fun handleClick(id: Long)
}