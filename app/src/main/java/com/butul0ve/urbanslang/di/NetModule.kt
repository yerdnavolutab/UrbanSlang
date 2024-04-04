package com.butul0ve.urbanslang.di

import com.butul0ve.urbanslang.network.AppNetworkHelper
import com.butul0ve.urbanslang.network.NetworkHelper
import com.butul0ve.urbanslang.network.ServerApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule(private val url: String) {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideServerApi(retrofit: Retrofit): ServerApi {
        return retrofit.create(ServerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkHelper(serverApi: ServerApi): NetworkHelper {
        return AppNetworkHelper(serverApi)
    }
}