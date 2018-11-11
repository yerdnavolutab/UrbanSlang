package com.butul0ve.urbanslang.mvp.cache

import android.content.Context
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
import com.butul0ve.urbanslang.db.AppDbHelper
import com.butul0ve.urbanslang.db.DbHelper
import com.butul0ve.urbanslang.db.UrbanDatabase
import com.butul0ve.urbanslang.mvp.FragmentCallback

private const val QUERY = "query_extra_key"

class CacheFragment : Fragment(), CacheMvpView {

    private lateinit var toolbar: Toolbar
    private lateinit var definitionsRV: RecyclerView
    private lateinit var noResultTV: TextView
    private lateinit var searchView: SearchView

    private lateinit var presenter: CacheMvpPresenter<CacheMvpView>
    private lateinit var callback: FragmentCallback
    private lateinit var dbHelper: DbHelper
    private lateinit var query: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = AppDbHelper(UrbanDatabase.getInstance(context!!)!!)
        try {
            callback = context as FragmentCallback
        } catch (ex: ClassCastException) {
            throw ClassCastException("${activity?.localClassName} must implement FragmentCallback")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        presenter = CachePresenter(dbHelper)
        presenter.onAttach(this)
        if (savedInstanceState == null && arguments == null) {
            presenter.loadAllCachedDefinitions()
        } else {
            if (savedInstanceState != null && savedInstanceState.containsKey(QUERY)) {
                query = savedInstanceState.getString(QUERY)
            } else if (arguments != null && arguments?.containsKey(QUERY)!!) {
                query = arguments?.getString(QUERY)!!
            }

            if (::query.isInitialized) {
                presenter.filterCachedDefinitions(query)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (searchView.query != null) {
            outState.putString(QUERY, searchView.query.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        if (arguments == null) {
            arguments = Bundle()
        }
        onSaveInstanceState(arguments!!)
    }

    override fun onDetach() {
        super.onDetach()
        if (::presenter.isInitialized) {
            presenter.onDetach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
        val search = menu?.findItem(R.id.action_search)
        searchView = search?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        if (::query.isInitialized && query.isNotEmpty()) {
            searchView.isIconified = false
            searchView.setQuery(query, false)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (::presenter.isInitialized) {

                    text?.let {
                        if (it.isEmpty()) {
                            presenter.loadAllCachedDefinitions()
                        } else {
                            presenter.filterCachedDefinitions(it)
                        }
                    }
                }
                return true
            }

        })

    }

    override fun showResultSearch(definitionAdapter: DefinitionAdapter) {
        noResultTV.visibility = View.GONE
        definitionsRV.visibility = View.VISIBLE
        definitionsRV.adapter = definitionAdapter
    }

    override fun showError() {
        noResultTV.visibility = View.VISIBLE
        definitionsRV.visibility = View.GONE
    }

    override fun onClick(definition: Definition) {
        callback.onDefinitionClick(definition)
    }

}