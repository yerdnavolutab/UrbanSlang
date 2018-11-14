package com.butul0ve.urbanslang.di

import dagger.Component

@Component(modules = [AppModule::class, DataModule::class, NetModule::class])
interface NetComponent {
}