package com.butul0ve.urbanslang.data

import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.data.db.DbHelper
import com.butul0ve.urbanslang.network.NetworkClient
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class DataRepoImpl @Inject constructor(
    private val dbHelper: DbHelper,
    private val networkClient: NetworkClient
) : DataRepo() {

    override suspend fun getDefinition(query: String): Result<BaseResponse> {
        return try {
            Result.success(networkClient.getDefinition(query))
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun getRandomDefinition(): Result<BaseResponse> {
        return try {
            Result.success(networkClient.getRandomDefinition())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
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