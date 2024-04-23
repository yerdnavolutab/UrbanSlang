package com.butul0ve.urbanslang.mvp.main.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.data.DataRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val dataRepo: DataRepo) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()


    fun onAction(action: Action) {
        when (action) {
            is Action.SetQuery -> {
                viewModelScope.launch {
                    val result = dataRepo.getDefinition(action.text).getOrNull() ?: return@launch
                    result.definitions.firstOrNull()?.let {
                        dataRepo.saveDefinition(it)
                    }
                }
            }

            is Action.GetData -> viewModelScope.launch {
                _state.update {
                    if (action.query.isBlank()) {
                        dataRepo.getRandomDefinition().getOrNull()?.let { data ->
                            State.Data(data.definitions)
                        } ?: State.Error

                    } else {
                        dataRepo.getDefinition().getOrNull()?.let { data ->
                            State.Data(data.definitions)
                        } ?: State.Error
                    }
                }
            }
        }
    }

    sealed interface State {

        data object Loading : State
        data object Error : State
        data class Data(val definitions: List<Definition>) : State
    }

    sealed interface Action {

        data class GetData(val query: String): Action

        data class SetQuery(val text: String) : Action
    }

    companion object {

        class MainViewModelFactory(private val dataRepo: DataRepo) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(dataRepo) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")

            }
        }
    }
}