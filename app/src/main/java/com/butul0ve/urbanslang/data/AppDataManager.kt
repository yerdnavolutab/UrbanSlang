package com.butul0ve.urbanslang.data

import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.data.db.DbHelper
import com.butul0ve.urbanslang.network.NetworkClient
import com.butul0ve.urbanslang.network.NetworkHelper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class AppDataManager @Inject constructor(
    val dbHelper: DbHelper,
    val networkHelper: NetworkHelper,
    private val networkClient: NetworkClient
) : DataManager() {


    override suspend fun getDataFromServer(query: String): BaseResponse {
        return networkClient.getDefinition(query)
    }

    override fun getDefinitions(): Flowable<List<Definition>> {
        return dbHelper.getDefinitions()
    }

    override fun getDefinitionById(id: Long?): Single<Definition> {
        return dbHelper.getDefinitionById(id)
    }

    override fun getFavoritesDefinitions(): Observable<List<Definition>> {
        return dbHelper.getFavoritesDefinitions()
    }

    override fun getCachedDefinitions(): Observable<List<Definition>> {
        return dbHelper.getCachedDefinitions()
    }

    override fun saveDefinition(definition: Definition): Single<Long> {
        return dbHelper.saveDefinition(definition)
    }

    override fun saveDefinitions(definitions: List<Definition>): Single<List<Long>> {
        return dbHelper.saveDefinitions(definitions)
    }

    override fun saveDefinitionToFavorites(definition: Definition): Completable {
        return dbHelper.saveDefinitionToFavorites(definition)
    }

    override fun deleteDefinition(definition: Definition): Completable {
        return dbHelper.deleteDefinition(definition)
    }

    override fun deleteDefinitionFromFavorites(definition: Definition): Completable {
        return dbHelper.deleteDefinitionFromFavorites(definition)
    }

    override fun deleteAllDefinitions(): Completable {
        return dbHelper.deleteAllDefinitions()
    }

    override fun deleteFavoritesDefinitions(): Completable {
        return dbHelper.deleteFavoritesDefinitions()
    }

    override fun deleteCachedDefinitions(): Completable {
        return dbHelper.deleteCachedDefinitions()
    }

    override fun findDefinition(permalink: String): Single<Definition> {
        return dbHelper.findDefinition(permalink)
    }

    override fun updateTempDefinitions(definitions: List<Definition>) {
        tempDefinitions.clear()
        tempDefinitions.addAll(definitions)
    }
}