package com.butul0ve.urbanslang.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.butul0ve.urbanslang.bean.Definition


@Database(entities = [Definition::class], version = 1)
abstract class UrbanDatabase: RoomDatabase() {

    abstract fun definitionDataDao(): DefinitionDao

    companion object {

        private var INSTANCE: UrbanDatabase? = null

        fun getInstance(context: Context): UrbanDatabase? {
            if (INSTANCE == null) {
                synchronized(UrbanDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UrbanDatabase::class.java, "urban.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}