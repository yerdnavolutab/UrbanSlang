package com.butul0ve.urbanslang.data.db

import androidx.room.*
import com.butul0ve.urbanslang.bean.Definition
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DefinitionDao {

    @Query("select * from definitions")
    fun getAll(): Flowable<List<Definition>>

    @Query("select * from definitions where id =:id")
    fun getDefinitionById(id: Long?): Single<Definition>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(definition: Definition): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(definitions: List<Definition>) : List<Long>

    @Query("delete from definitions")
    fun deleteAll()

    @Query("select * from definitions where favorite = :favorite")
    fun getAllFavorites(favorite: Int = 1): List<Definition>

    @Query("select * from definitions where favorite = :favorite")
    fun getAllCached(favorite: Int = 0): List<Definition>

    @Delete
    fun delete(definition: Definition)

    @Query("delete from definitions where favorite = :favorite")
    fun deleteAllFavorites(favorite: Int = 1)

    @Query("delete from definitions where favorite = :favorite")
    fun deleteCachedDefinitions(favorite: Int = 0)

    @Query("select * from definitions where permalink =:permalink limit 1")
    fun findDefinitionByLink(permalink: String): Single<Definition>
}