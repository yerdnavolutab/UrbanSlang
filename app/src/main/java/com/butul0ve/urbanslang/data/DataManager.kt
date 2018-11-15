package com.butul0ve.urbanslang.data

import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.bean.Definition
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


abstract class DataManager {

    val tempDefinitions = ArrayList<Definition>()

    abstract fun getDataFromServer(query: String = ""): Single<BaseResponse>

    abstract fun getDefinitions(): Flowable<List<Definition>>

    abstract fun getDefinitionById(id: Long?): Single<Definition>

    abstract fun getFavoritesDefinitions(): Observable<List<Definition>>

    abstract fun getCachedDefinitions(): Observable<List<Definition>>

    abstract fun saveDefinition(definition: Definition): Single<Long>

    abstract fun saveDefinitions(definitions: List<Definition>): Single<List<Long>>

    abstract fun saveDefinitionToFavorites(definition: Definition): Completable

    abstract fun deleteDefinition(definition: Definition): Completable

    abstract fun deleteDefinitionFromFavorites(definition: Definition): Completable

    abstract fun deleteAllDefinitions(): Completable

    abstract fun deleteFavoritesDefinitions(): Completable

    abstract fun deleteCachedDefinitions(): Completable

    abstract fun findDefinition(permalink: String): Single<Definition>

    abstract fun updateTempDefinitions(definitions: List<Definition>)
}