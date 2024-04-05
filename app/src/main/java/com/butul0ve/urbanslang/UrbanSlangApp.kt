package com.butul0ve.urbanslang

import androidx.multidex.MultiDexApplication
import com.butul0ve.urbanslang.di.*

class UrbanSlangApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule(BASE_URL))
            .dataModule(DataModule())
            .build()
    }

    companion object {

        private const val BASE_URL = "https://api.urbandictionary.com/"

        lateinit var netComponent: NetComponent
    }
}