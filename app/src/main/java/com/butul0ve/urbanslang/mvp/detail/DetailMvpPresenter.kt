package com.butul0ve.urbanslang.mvp.detail

import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.MvpPresenter

interface DetailMvpPresenter<V: DetailMvpView>: MvpPresenter<V> {

    fun onViewInitialized(id: Long)

    fun handleClick(definition: Definition)
}