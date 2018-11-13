package com.butul0ve.urbanslang.mvp.favorites

import com.butul0ve.urbanslang.mvp.MvpPresenter

interface FavoritesMvpPresenter<V: FavoritesMvpView>: MvpPresenter<V> {

    fun loadAllFavoritesDefinitions()

    fun filterFavoritesDefinitions(query: String)

    fun clearFavorites()
}