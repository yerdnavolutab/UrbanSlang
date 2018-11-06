package com.butul0ve.urbanslang.mvp.main

import android.util.Log
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.adapter.DefinitionClickListener
import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.mvp.BasePresenter
import com.butul0ve.urbanslang.network.AppNetworkHelper
import com.butul0ve.urbanslang.network.NetworkHelper
import com.butul0ve.urbanslang.network.ServerApi
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainPresenter<V : MainMvpView> : BasePresenter<V>(), MainMvpPresenter<V>, DefinitionClickListener {

    private val networkHelper: NetworkHelper
    private lateinit var definitionAdapter: DefinitionAdapter

    init {
        val url = "http://api.urbandictionary.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        networkHelper = AppNetworkHelper(retrofit.create(ServerApi::class.java))
    }

    override fun onViewInitialized() {
        networkHelper.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    override fun getData(query: String) {
        networkHelper.getData(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    override fun onItemClick(position: Int) {
        if (::definitionAdapter.isInitialized) {
            if (isViewAttached()) {
                val definition = definitionAdapter.getDefinition(position)
                mvpView?.onClick(definition)
            }
        }
    }

    private fun getObserver(): SingleObserver<BaseResponse> {
        return object : SingleObserver<BaseResponse> {

            override fun onSuccess(t: BaseResponse) {
                if (isViewAttached()) {
                    definitionAdapter = DefinitionAdapter(t.definitions, this@MainPresenter)
                    Log.d("main presenter", "${t.definitions.size}")
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