package com.butul0ve.urbanslang.mvvm.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.adapter.DefinitionClickListener
import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.data.DataManager
import com.butul0ve.urbanslang.mvvm.Event
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val dataManager: DataManager
): ViewModel() {

    private val _definitionAdapter = MutableLiveData<DefinitionAdapter>()
    val definitionAdapter: LiveData<DefinitionAdapter> = _definitionAdapter

    private val definitionClickListener: DefinitionClickListener by lazy {
        object : DefinitionClickListener {
            override fun onItemClick(position: Int) {
                _onClick.value = Event(_definitionAdapter.value?.definitions?.get(position)?.id ?: -1)
            }
        }
    }

    private val _onClick = MutableLiveData<Event<Long>>()
    val onClick: LiveData<Event<Long>> = _onClick

    private val _query = MutableLiveData("")
    val query: LiveData<String> = _query

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNoResult = MutableLiveData(false)
    val isNoResult: LiveData<Boolean> = _isNoResult

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun getData(query: String) {
        dataManager.tempDefinitions.clear()
        _query.value = query
        dataManager.getDataFromServer(query)
            .flatMap { saveDefinitions(it) }
            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( object : SingleObserver<List<Definition>> {
                override fun onSuccess(list: List<Definition>) {
                    dataManager.updateTempDefinitions(list)
                    _definitionAdapter.postValue(DefinitionAdapter(list, definitionClickListener))
                    _isLoading.postValue(false)
                }

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    _isLoading.postValue(true)
                }

                override fun onError(e: Throwable) {
                    // todo: handle error
                    _isLoading.postValue(false)
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun saveDefinitions(response: BaseResponse): SingleSource<List<Definition>> {
        val result = ArrayList<Definition>()
        response.definitions.forEach { definition ->
            disposable.add(
                dataManager.findDefinition(definition.permalink)
                    .subscribe(
                        {
                            result.add(it)
                        },
                        {
                            dataManager.saveDefinition(definition).subscribe({ id ->
                                definition.id = id
                                result.add(definition)
                            },
                                {
                                    Log.d("main vm", "error saving definition")
                                    // todo: handle error
                                })

                        })
            )
        }
        return Single.just(result)
    }
}