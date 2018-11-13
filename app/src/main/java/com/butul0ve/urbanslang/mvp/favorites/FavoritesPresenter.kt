package com.butul0ve.urbanslang.mvp.favorites

import android.util.Log
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.adapter.DefinitionClickListener
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.db.DbHelper
import com.butul0ve.urbanslang.mvp.BasePresenter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoritesPresenter<V : FavoritesMvpView>() : BasePresenter<V>(), FavoritesMvpPresenter<V>,
    DefinitionClickListener {

    private lateinit var definitionAdapter: DefinitionAdapter
    private lateinit var dbHelper: DbHelper

    constructor(dbHelper: DbHelper) : this() {
        this.dbHelper = dbHelper
    }

    override fun loadAllFavoritesDefinitions() {
        dbHelper.getFavoritesDefinitions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    override fun filterFavoritesDefinitions(query: String) {
        dbHelper.getFavoritesDefinitions()
            .subscribeOn(Schedulers.io())
            .map {
                it.filter { it.word.contains(query, true) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    override fun clearFavorites() {
        disposable.add(dbHelper.deleteFavoritesDefinitions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (isViewAttached()) {
                    mvpView?.showSuccessSnackbar()
                }
            })
    }

    override fun onItemClick(position: Int) {
        if (::definitionAdapter.isInitialized) {
            val definition = definitionAdapter.definitions[position]
            mvpView?.onClick(definition)
        }
    }

    private fun getObserver(): Observer<List<Definition>> {
        return object : Observer<List<Definition>> {

            override fun onComplete() {
                Log.d("favorites presenter", "getObserver onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onNext(t: List<Definition>) {
                if (isViewAttached()) {
                    if (::definitionAdapter.isInitialized) {
                        definitionAdapter.updateDefinitions(t)
                    } else {
                        definitionAdapter = DefinitionAdapter(t, this@FavoritesPresenter)
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
}