package com.butul0ve.urbanslang.mvp.cache

import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.MvpView

interface CacheMvpView: MvpView {

    fun showResultSearch(definitionAdapter: DefinitionAdapter)

    fun showError()

    fun onClick(definition: Definition)

    fun showSuccessSnackbar()
}