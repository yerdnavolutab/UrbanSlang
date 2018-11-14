package com.butul0ve.urbanslang.di

import android.content.Context
import com.butul0ve.urbanslang.db.AppDbHelper
import com.butul0ve.urbanslang.db.DbHelper
import com.butul0ve.urbanslang.db.UrbanDatabase
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
}