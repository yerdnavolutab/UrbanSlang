package com.butul0ve.urbanslang.mvp

import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalStateException

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach()
 */
open class BasePresenter<V : MvpView> : MvpPresenter<V> {

    var mvpView: V? = null
        private set

    protected lateinit var disposable: CompositeDisposable

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
        disposable = CompositeDisposable()
    }

    override fun onDetach() {
        mvpView = null
        if (::disposable.isInitialized) {
            disposable.clear()
        } else {
            throw IllegalStateException("view must be attached")
        }
    }

    /**
     * This method checks attached view.
     * If it is attached, @return true or false if not
     */
    fun isViewAttached(): Boolean {
        return mvpView != null
    }
}