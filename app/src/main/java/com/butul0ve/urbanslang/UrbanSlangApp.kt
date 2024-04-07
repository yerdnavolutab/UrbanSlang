package com.butul0ve.urbanslang

import androidx.multidex.MultiDexApplication
import com.butul0ve.urbanslang.data.DataRepo
import com.butul0ve.urbanslang.di.*
import javax.inject.Inject

class UrbanSlangApp: MultiDexApplication() {

    @Inject lateinit var dataRepo: DataRepo

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule(BASE_URL))
            .dataModule(DataModule())
            .build()
        netComponent.inject(this)
    }

    companion object {

        private const val BASE_URL = "https://api.urbandictionary.com/"

        lateinit var netComponent: NetComponent
    }
}