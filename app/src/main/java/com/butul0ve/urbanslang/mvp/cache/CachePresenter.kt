package com.butul0ve.urbanslang.mvp.cache

import android.util.Log
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.adapter.DefinitionClickListener
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.data.DataRepo
import com.butul0ve.urbanslang.mvp.BasePresenter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CachePresenter<V : CacheMvpView> @Inject constructor(val dataManager: DataRepo) : BasePresenter<V>(),
    CacheMvpPresenter<V>, DefinitionClickListener {

    private lateinit var definitionAdapter: DefinitionAdapter

    override fun loadAllCachedDefinitions() {
        if (isViewAttached()) {
            dataManager.getCachedDefinitions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver())
        }
    }


    override fun filterCachedDefinitions(query: String) {
        if (isViewAttached()) {
            dataManager.getCachedDefinitions()
                .subscribeOn(Schedulers.io())
                .map {
                    it.filter { it.word.contains(query, true) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver())
        }
    }

    override fun clearCache() {
        disposable.add(dataManager.deleteCachedDefinitions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (isViewAttached()) {
                    mvpView?.showSuccessSnackbar()
                }
            })
    }

    private fun getObserver(): Observer<List<Definition>> {
        return object : Observer<List<Definition>> {

            override fun onComplete() {
                Log.d("cache presenter", "getObserver onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onNext(t: List<Definition>) {
                if (isViewAttached()) {
                    if (::definitionAdapter.isInitialized) {
                        definitionAdapter.updateDefinitions(t)
                    } else {
                        definitionAdapter = DefinitionAdapter(t, this@CachePresenter)
                    }
                    mvpView?.showResultSearch(definitionAdapter)
                }
            }

            override fun onError(e: Throwable) {
                if (isViewAttached()) {
                    mvpView?.showError()
                }
            }

        }
    }

    override fun onItemClick(position: Int) {
        if (::definitionAdapter.isInitialized) {
            if (isViewAttached()) {
                val definition = definitionAdapter.definitions[position]
                mvpView?.onClick(definition)
            }
        }
    }
}