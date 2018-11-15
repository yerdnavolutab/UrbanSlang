package com.butul0ve.urbanslang.data.db

import com.butul0ve.urbanslang.bean.Definition
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

class AppDbHelper(private val db: UrbanDatabase) : DbHelper {

    override fun getDefinitions(): Flowable<List<Definition>> {
        return db.definitionDataDao().getAll()
    }

    override fun getDefinitionById(id: Long?): Single<Definition> {
        return db.definitionDataDao().getDefinitionById(id)
    }

    override fun findDefinition(permalink: String): Single<Definition> {
        return db.definitionDataDao().findDefinitionByLink(permalink)
    }

    override fun getFavoritesDefinitions(): Observable<List<Definition>> {
        return Observable.fromCallable { db.definitionDataDao().getAllFavorites() }
    }

    override fun getCachedDefinitions(): Observable<List<Definition>> {
        return Observable.fromCallable { db.definitionDataDao().getAllCached() }
    }

    override fun saveDefinition(definition: Definition): Single<Long> {
        return Single.fromCallable { db.definitionDataDao().insert(definition) }
    }

    override fun saveDefinitions(definitions: List<Definition>): Single<List<Long>> {
        return Single.fromCallable { db.definitionDataDao().insert(definitions) }
    }

    override fun saveDefinitionToFavorites(definition: Definition): Completable {
        return Completable.fromAction {
            definition.favorite = 1
            db.definitionDataDao().insert(definition)
        }
    }

    override fun deleteDefinition(definition: Definition): Completable {
        return Completable.fromAction { db.definitionDataDao().delete(definition) }
    }

    /**
     * There is no need to delete the definition at all, so
     * just change the flag and update the record in db
     */
    override fun deleteDefinitionFromFavorites(definition: Definition): Completable {
        return Completable.fromAction {
            definition.favorite = 1
            db.definitionDataDao().insert(definition)
        }
    }

    override fun deleteAllDefinitions(): Completable {
        return Completable.fromAction { db.definitionDataDao().deleteAll() }
    }

    override fun deleteFavoritesDefinitions(): Completable {
        return Completable.fromAction { db.definitionDataDao().deleteAllFavorites() }
    }

    override fun deleteCachedDefinitions(): Completable {
        return Completable.fromAction { db.definitionDataDao().deleteCachedDefinitions() }
    }

}