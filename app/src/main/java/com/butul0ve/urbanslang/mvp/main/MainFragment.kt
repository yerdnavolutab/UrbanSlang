package com.butul0ve.urbanslang.mvp.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import com.butul0ve.urbanslang.R
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.bean.Definition

class MainFragment : Fragment(), MainMvpView {

    private lateinit var toolbar: Toolbar
    private lateinit var definitionsRV: RecyclerView
    private lateinit var noResultTV: TextView
    private lateinit var presenter: MainMvpPresenter<MainMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        presenter = MainPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        definitionsRV = view.findViewById(R.id.definitions_RV)
        noResultTV = view.findViewById(R.id.no_results_TV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        if (::presenter.isInitialized) {
            presenter.onAttach(this)
            presenter.onViewInitialized()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::presenter.isInitialized) {
            presenter.onDetach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                text?.let { presenter.getData(it) }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }

    override fun showResultSearch(adapter: DefinitionAdapter) {
        definitionsRV.visibility = View.VISIBLE
        noResultTV.visibility = View.GONE
        definitionsRV.adapter = adapter
    }

    override fun showError() {
        definitionsRV.visibility = View.GONE
        noResultTV.visibility = View.VISIBLE
    }

    override fun onClick(definition: Definition) {
        TODO("invoke method from main activity to open detail fragment")
    }
}