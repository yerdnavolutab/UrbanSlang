package com.butul0ve.urbanslang.mvp.main

import android.util.Log
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.adapter.DefinitionClickListener
import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.db.DbHelper
import com.butul0ve.urbanslang.mvp.BasePresenter
import com.butul0ve.urbanslang.network.AppNetworkHelper
import com.butul0ve.urbanslang.network.NetworkHelper
import com.butul0ve.urbanslang.network.ServerApi
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainPresenter<V : MainMvpView>() : BasePresenter<V>(), MainMvpPresenter<V>, DefinitionClickListener {

    private val networkHelper: NetworkHelper
    private lateinit var definitionAdapter: DefinitionAdapter
    private lateinit var dbHelper: DbHelper

    constructor(dbHelper: DbHelper, definitions: List<Definition>) : this() {
        definitionAdapter = DefinitionAdapter(definitions, this)
        this.dbHelper = dbHelper
    }

    constructor(dbHelper: DbHelper) : this() {
        this.dbHelper = dbHelper
    }

    init {
        val url = "http://api.urbandictionary.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        networkHelper = AppNetworkHelper(retrofit.create(ServerApi::class.java))
    }

    override fun onFirstViewInitialized() {
        networkHelper.getData()
            .flatMap { saveDefinitions(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    override fun onViewInitialized() {
        if (isViewAttached()) {
            if (::definitionAdapter.isInitialized) {
                mvpView?.showResultSearch(definitionAdapter)
            } else {
                mvpView?.showError()
            }
        }
    }

    override fun getData(query: String) {
        networkHelper.getData(query)
            .flatMap { saveDefinitions(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    private fun saveDefinitions(response: BaseResponse): SingleSource<List<Definition>> {
        val result = ArrayList<Definition>()
        response.definitions.forEach { definition ->
            disposable.add(
                dbHelper.findDefinition(definition.permalink)
                    .subscribe({
                        result.add(it)
                    },
                        {
                            Log.d("main presenter", "error getting definition")
                            dbHelper.saveDefinition(definition).subscribe({ id ->
                                definition.id = id
                                result.add(definition)
                            },
                                {
                                    Log.d("main presenter", "error saving definition")
                                })

                        })
            )
        }
        return Single.just(result)
    }

    override fun getDefinitions(): List<Definition>? {
        return if (::definitionAdapter.isInitialized)
            definitionAdapter.definitions
        else null
    }

    override fun onItemClick(position: Int) {
        if (::definitionAdapter.isInitialized) {
            if (isViewAttached()) {
                val definition = definitionAdapter.definitions[position]
                mvpView?.onClick(definition)
            }
        }
    }

    private fun getObserver(): SingleObserver<List<Definition>> {

        return object : SingleObserver<List<Definition>> {
            override fun onSuccess(t: List<Definition>) {
                if (isViewAttached()) {

                    definitionAdapter = DefinitionAdapter(t, this@MainPresenter)
                    Log.d("main presenter", "${t.size}")
                    mvpView?.showResultSearch(definitionAdapter)
                } else {
                    Log.d("main presenter", "onSuccess view is not attached")
                }
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onError(e: Throwable) {
                if (isViewAttached()) {
                    mvpView?.showError()
                    Log.d("main presenter", "onError")
                } else {
                    Log.d("main presenter", "onError view is not attached")
                }
            }

        }
    }
}