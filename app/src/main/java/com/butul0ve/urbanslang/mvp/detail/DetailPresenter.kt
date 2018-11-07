package com.butul0ve.urbanslang.mvp.detail

import android.util.Log
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.db.DbHelper
import com.butul0ve.urbanslang.mvp.BasePresenter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailPresenter<V : DetailMvpView>(private val dbHelper: DbHelper) : BasePresenter<V>(), DetailMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        disposable.add(dbHelper.getDefinitions()
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d("detail presenter", "all definitions:")
                it.forEach { Log.d("detail presenter", it.word) }
            })
    }

    override fun onViewInitialized(id: Long) {
        disposable.add(dbHelper.getDefinitionById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (isViewAttached()) {
                    if (it.favorite == 1) {
                        mvpView?.setFav(true)
                    } else {
                        mvpView?.setFav(false)
                    }
                }
            },
                {
                    Log.d("detail presenter", "error getting definition from onViewInitialized method")
                }))
    }

    override fun handleClick(definition: Definition) {
        dbHelper.getDefinitionById(definition.id)
            .subscribeOn(Schedulers.io())
            .subscribe(getObserver())

        disposable.add(dbHelper.getFavoritesDefinitions()
            .subscribeOn(Schedulers.io())
            .subscribe{
                Log.d("detail presenter", "favorites definitions:")
                it.forEach { Log.d("detail presenter", it.word) }
            })
    }

    private fun getObserver(): SingleObserver<Definition> {
        return object : SingleObserver<Definition> {

            override fun onSuccess(t: Definition) {
                if (t.favorite == 1)
                    t.favorite = 0
                else
                    t.favorite = 1

                disposable.add(
                    dbHelper.saveDefinition(t)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (isViewAttached()) {
                                mvpView?.changeFavImg()
                            }
                        },
                            {
                                Log.d("DetailPresenter", "error saving definition")
                            })
                )
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onError(e: Throwable) {
                Log.d("DetailPresenter", "error getting definition")
            }

        }
    }
}