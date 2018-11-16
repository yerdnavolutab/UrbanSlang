package com.butul0ve.urbanslang.mvp.main

import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.MvpView

interface MainMvpView: MvpView {

    fun showResultSearch(adapter: DefinitionAdapter)

    fun showError()

    fun onClick(definition: Definition)

    fun showProgressbar()
}