package com.butul0ve.urbanslang.mvp

import io.reactivex.disposables.CompositeDisposable

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach()
 */
open class BasePresenter<V : MvpView> : MvpPresenter<V> {

    var mvpView: V? = null
        private set

    protected val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun onDetach() {
        mvpView = null
        disposable.clear()
    }

    /**
     * This method checks attached view.
     * If it is attached, @return true or false if not
     */
    fun isViewAttached(): Boolean {
        return mvpView != null
    }
}