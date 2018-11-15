package com.butul0ve.urbanslang.di

import android.content.Context
import com.butul0ve.urbanslang.data.AppDataManager
import com.butul0ve.urbanslang.data.DataManager
import com.butul0ve.urbanslang.data.db.AppDbHelper
import com.butul0ve.urbanslang.data.db.DbHelper
import com.butul0ve.urbanslang.data.db.UrbanDatabase
import com.butul0ve.urbanslang.mvp.cache.CacheMvpPresenter
import com.butul0ve.urbanslang.mvp.cache.CacheMvpView
import com.butul0ve.urbanslang.mvp.cache.CachePresenter
import com.butul0ve.urbanslang.mvp.detail.DetailMvpPresenter
import com.butul0ve.urbanslang.mvp.detail.DetailMvpView
import com.butul0ve.urbanslang.mvp.detail.DetailPresenter
import com.butul0ve.urbanslang.mvp.favorites.FavoritesMvpPresenter
import com.butul0ve.urbanslang.mvp.favorites.FavoritesMvpView
import com.butul0ve.urbanslang.mvp.favorites.FavoritesPresenter
import com.butul0ve.urbanslang.mvp.main.MainMvpPresenter
import com.butul0ve.urbanslang.mvp.main.MainMvpView
import com.butul0ve.urbanslang.mvp.main.MainPresenter
import com.butul0ve.urbanslang.mvp.trends.TrendsMvpPresenter
import com.butul0ve.urbanslang.mvp.trends.TrendsMvpView
import com.butul0ve.urbanslang.mvp.trends.TrendsPresenter
import com.butul0ve.urbanslang.network.NetworkHelper
import com.butul0ve.urbanslang.utils.readDictionaryFromAssets
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

    @Provides
    @Singleton
    fun provideDetailPresenter(dataManager: DataManager): DetailMvpPresenter<DetailMvpView> {
        return DetailPresenter(dataManager)
    }

    @Provides
    @Singleton
    fun provideFavoritesPresenter(dataManager: DataManager): FavoritesMvpPresenter<FavoritesMvpView> {
        return FavoritesPresenter(dataManager)
    }

    @Provides
    @Singleton
    fun provideCachePresenter(dataManager: DataManager): CacheMvpPresenter<CacheMvpView> {
        return CachePresenter(dataManager)
    }

    @Provides
    @Singleton
    fun provideDictionary(context: Context): MutableMap<String, List<String>> {
        return readDictionaryFromAssets(context)
    }

    @Provides
    @Singleton
    fun provideTrendsPresenter(dictionary: MutableMap<String, List<String>>): TrendsMvpPresenter<TrendsMvpView> {
        return TrendsPresenter(dictionary)
    }
}