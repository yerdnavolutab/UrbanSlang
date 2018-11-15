package com.butul0ve.urbanslang.di

import com.butul0ve.urbanslang.mvp.detail.DetailFragment
import com.butul0ve.urbanslang.mvp.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetModule::class])
interface NetComponent {

    fun inject(mainFragment: MainFragment)

    fun inject(detailFragment: DetailFragment)
}