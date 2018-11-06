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

private const val DEFINITIONS = "definitions_extra_key"
private const val QUERY = "query_extra_key"

class MainFragment : Fragment(), MainMvpView {

    private lateinit var toolbar: Toolbar
    private lateinit var definitionsRV: RecyclerView
    private lateinit var noResultTV: TextView
    private lateinit var presenter: MainMvpPresenter<MainMvpView>
    private lateinit var searchView: SearchView

    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            presenter = MainPresenter()
        } else {
            if (savedInstanceState.containsKey(DEFINITIONS)) {
                val list = savedInstanceState.getParcelableArray(DEFINITIONS) as Array<Definition>
                presenter = MainPresenter(list.asList())
            }
        }
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
            if (savedInstanceState == null) {
                presenter.onFirstViewInitialized()
            } else {
                presenter.onViewInitialized()
            }
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(QUERY)) {
            query = savedInstanceState.getString(QUERY)
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
        searchView = search?.actionView as SearchView
        if (::query.isInitialized && query.isNotEmpty()) {
            searchView.isIconified = false
            searchView.setQuery(query, false)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                if (::presenter.isInitialized) {
                    presenter.getData(text)
                } else {
                    presenter = MainPresenter()
                    presenter.onAttach(this@MainFragment)
                    presenter.getData(text)
                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::presenter.isInitialized) {
            val definitions = presenter.getDefinitions()
            if (definitions != null) {
                outState.putParcelableArray(DEFINITIONS, definitions.toTypedArray())
            }
        }

        if (searchView.query != null) {
            outState.putString(QUERY, searchView.query.toString())
        }
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