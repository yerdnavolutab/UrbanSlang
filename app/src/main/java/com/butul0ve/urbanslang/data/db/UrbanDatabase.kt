package com.butul0ve.urbanslang.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.butul0ve.urbanslang.bean.Definition


@Database(entities = [Definition::class], version = 1, exportSchema = false)
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