package com.butul0ve.urbanslang.mvp

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * class indicating the MvpView type that wants to be attached with
 * @see BasePresenter
 */
interface MvpPresenter<V: MvpView> {

    /**
     * Attach the View to Presenter
     */
    fun onAttach(mvpView: V)

    /**
     * Detach the View from Presenter
     */
    fun onDetach()
}