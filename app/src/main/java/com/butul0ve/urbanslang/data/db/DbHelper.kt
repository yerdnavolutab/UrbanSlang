package com.butul0ve.urbanslang.data.db

import com.butul0ve.urbanslang.bean.Definition
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface DbHelper {

    fun getDefinitions(): Flowable<List<Definition>>

    fun getDefinitionById(id: Long?): Single<Definition>

    fun getFavoritesDefinitions(): Observable<List<Definition>>

    fun getCachedDefinitions(): Observable<List<Definition>>

    fun saveDefinition(definition: Definition): Single<Long>

    fun saveDefinitions(definitions: List<Definition>): Single<List<Long>>

    fun saveDefinitionToFavorites(definition: Definition): Completable

    fun deleteDefinition(definition: Definition): Completable

    fun deleteDefinitionFromFavorites(definition: Definition): Completable

    fun deleteAllDefinitions(): Completable

    fun deleteFavoritesDefinitions(): Completable

    fun deleteCachedDefinitions(): Completable

    fun findDefinition(permalink: String): Single<Definition>
}