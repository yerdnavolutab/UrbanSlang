package com.butul0ve.urbanslang.di

import android.content.Context
import com.butul0ve.urbanslang.data.AppDataManager
import com.butul0ve.urbanslang.data.DataManager
import com.butul0ve.urbanslang.data.db.AppDbHelper
import com.butul0ve.urbanslang.data.db.DbHelper
import com.butul0ve.urbanslang.data.db.UrbanDatabase
import com.butul0ve.urbanslang.mvp.main.MainMvpPresenter
import com.butul0ve.urbanslang.mvp.main.MainMvpView
import com.butul0ve.urbanslang.mvp.main.MainPresenter
import com.butul0ve.urbanslang.network.NetworkHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideUrbanDatabase(context: Context): UrbanDatabase {
        return UrbanDatabase.getInstance(context)!!
    }

    @Provides
    @Singleton
    fun provideDbHelper(db: UrbanDatabase): DbHelper {
        return AppDbHelper(db)
    }

    @Provides
    @Singleton
    fun provideDataManager(dbHelper: DbHelper, networkHelper: NetworkHelper): DataManager {
        return AppDataManager(dbHelper, networkHelper)
    }

    @Provides
    @Singleton
    fun provideMainPresenter(dataManager: DataManager): MainMvpPresenter<MainMvpView> {
        return MainPresenter(dataManager)
    }
}