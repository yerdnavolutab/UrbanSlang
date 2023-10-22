package com.butul0ve.urbanslang.mvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.butul0ve.urbanslang.data.DataManager
import com.butul0ve.urbanslang.mvvm.main.MainViewModel

class ViewModelFactory(
    private val dataManager: DataManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(dataManager) as T
    }
}