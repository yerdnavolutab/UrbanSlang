package com.butul0ve.urbanslang.mvp.main

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

private const val DEFINITIONS = "definitions_extra_key"
private const val QUERY = "query_extra_key"

class MainFragment : Fragment(), MainMvpView {

    private lateinit var toolbar: Toolbar
    private lateinit var definitionsRV: RecyclerView
    private lateinit var noResultTV: TextView
    private lateinit var presenter: MainMvpPresenter<MainMvpView>
    private lateinit var searchView: SearchView

    private lateinit var dbHelper: DbHelper
    private lateinit var query: String
    private lateinit var callback: FragmentCallback

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

        if (savedInstanceState == null) {

            if (arguments == null) {
                presenter = MainPresenter(dbHelper)
                presenter.onAttach(this)
                presenter.onFirstViewInitialized()

            } else {

                if (arguments!!.containsKey(DEFINITIONS)) {
                    val list = arguments!!.getParcelableArray(DEFINITIONS) as Array<Definition>
                    presenter = MainPresenter(dbHelper, list.asList())
                    presenter.onAttach(this)
                    presenter.onViewInitialized()
                }

                if (arguments!!.containsKey(QUERY)) {
                    presenter = MainPresenter(dbHelper)
                    presenter.onAttach(this)
                    query = arguments!!.getString(QUERY)
                    presenter.getData(query)
                }
            }
        } else {

            if (savedInstanceState.containsKey(DEFINITIONS)) {
                val list = savedInstanceState.getParcelableArray(DEFINITIONS) as Array<Definition>
                presenter = MainPresenter(dbHelper, list.asList())
                presenter.onAttach(this)
                presenter.onViewInitialized()

            }

            if (savedInstanceState.containsKey(QUERY)) {
                query = savedInstanceState.getString(QUERY)
            }
        }
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

        if (::searchView.isInitialized && searchView.query != null) {
            outState.putString(QUERY, searchView.query.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        if (::presenter.isInitialized) {
            if (arguments == null) {
                arguments = Bundle()
            }
            onSaveInstanceState(arguments!!)
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
        callback.onDefinitionClick(definition)
    }

    companion object {

        fun newInstance(word: String): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString(QUERY, word)
            fragment.arguments = args
            return fragment
        }
    }
}