package com.butul0ve.urbanslang.mvp

import com.butul0ve.urbanslang.bean.Definition

interface FragmentCallback {

    fun onDefinitionClick(definition: Definition)

    fun onMenuToolbarClick()
}