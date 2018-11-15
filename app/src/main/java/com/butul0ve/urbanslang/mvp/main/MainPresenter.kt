package com.butul0ve.urbanslang.mvp.main

import android.util.Log
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.adapter.DefinitionClickListener
import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.data.DataManager
import com.butul0ve.urbanslang.data.db.DbHelper
import com.butul0ve.urbanslang.mvp.BasePresenter
import com.butul0ve.urbanslang.network.NetworkHelper
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter<V : MainMvpView> @Inject constructor(val dataManager: DataManager) : BasePresenter<V>(),
    MainMvpPresenter<V>, DefinitionClickListener {

    private lateinit var definitionAdapter: DefinitionAdapter

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        if (dataManager.tempDefinitions.isEmpty()) {
            getData()
        } else {
            definitionAdapter = DefinitionAdapter(dataManager.tempDefinitions, this)
            mvpView.showResultSearch(definitionAdapter)
        }
    }

    override fun getData(query: String) {
        dataManager.tempDefinitions.clear()
        dataManager.getDataFromServer(query)
            .flatMap { saveDefinitions(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    override fun onItemClick(position: Int) {
        if (::definitionAdapter.isInitialized) {
            if (isViewAttached()) {
                val definition = definitionAdapter.definitions[position]
                mvpView?.onClick(definition)
            }
        }
    }

    private fun getObserver(): SingleObserver<List<Definition>> {

        return object : SingleObserver<List<Definition>> {
            override fun onSuccess(t: List<Definition>) {
                dataManager.updateTempDefinitions(t)
                definitionAdapter = DefinitionAdapter(t, this@MainPresenter)
                if (isViewAttached()) {
                    mvpView?.showResultSearch(definitionAdapter)
                } else {
                    Log.d("main presenter", "onSuccess view is not attached")
                }
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onError(e: Throwable) {
                if (isViewAttached()) {
                    mvpView?.showError()
                } else {
                    Log.d("main presenter", "onError view is not attached")
                }
            }

        }
    }

    private fun saveDefinitions(response: BaseResponse): SingleSource<List<Definition>> {
        val result = ArrayList<Definition>()
        response.definitions.forEach { definition ->
            disposable.add(
                dataManager.findDefinition(definition.permalink)
                    .subscribe({
                        result.add(it)
                    },
                        {
                            dataManager.saveDefinition(definition).subscribe({ id ->
                                definition.id = id
                                result.add(definition)
                            },
                                {
                                    Log.d("main presenter", "error saving definition")
                                })

                        })
            )
        }
        return Single.just(result)
    }
}