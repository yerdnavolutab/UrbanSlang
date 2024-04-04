package com.butul0ve.urbanslang.mvp.detail

import android.util.Log
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.data.DataManager
import com.butul0ve.urbanslang.mvp.BasePresenter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailPresenter<V : DetailMvpView> @Inject constructor(val dataManager: DataManager) : BasePresenter<V>(),
    DetailMvpPresenter<V> {

    override fun loadDefinition(id: Long) {
        dataManager.getDefinitionById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    override fun handleClick(id: Long) {
        dataManager.getDefinitionById(id)
            .subscribeOn(Schedulers.io())
            .subscribe(getClickObserver())
    }

    private fun getObserver(): SingleObserver<Definition> {
        return object : SingleObserver<Definition> {

            override fun onSuccess(definition: Definition) {
                if (isViewAttached()) {
                    mvpView?.setDefinition(definition)

                    if (definition.favorite == 1) {
                        mvpView?.setFav(true)
                    } else {
                        mvpView?.setFav(false)
                    }
                }
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onError(e: Throwable) {
                Log.d("detail presenter", "error getting definition from loadDefinition method")
            }
        }
    }

    private fun getClickObserver(): SingleObserver<Definition> {
        return object : SingleObserver<Definition> {

            override fun onSuccess(t: Definition) {
                if (t.favorite == 1)
                    t.favorite = 0
                else
                    t.favorite = 1

                disposable.add(
                    dataManager.saveDefinition(t)
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