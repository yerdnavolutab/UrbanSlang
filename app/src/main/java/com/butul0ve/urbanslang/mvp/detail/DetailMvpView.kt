package com.butul0ve.urbanslang.mvp.detail

import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.MvpView

interface DetailMvpView: MvpView {

    fun changeFavImg()

    fun setFav(isFav: Boolean)

    fun setDefinition(definition: Definition)
}