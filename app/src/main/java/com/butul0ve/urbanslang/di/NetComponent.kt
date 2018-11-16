package com.butul0ve.urbanslang.di

import com.butul0ve.urbanslang.mvp.cache.CacheFragment
import com.butul0ve.urbanslang.mvp.detail.DetailFragment
import com.butul0ve.urbanslang.mvp.favorites.FavoritesFragment
import com.butul0ve.urbanslang.mvp.main.MainFragment
import com.butul0ve.urbanslang.mvp.trends.TrendsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetModule::class])
interface NetComponent {

    fun inject(mainFragment: MainFragment)

    fun inject(detailFragment: DetailFragment)

    fun inject(favoritesFragment: FavoritesFragment)

    fun inject(cacheFragment: CacheFragment)

    fun inject(trendsFragment: TrendsFragment)
}