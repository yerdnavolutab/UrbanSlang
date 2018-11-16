package com.butul0ve.urbanslang.mvp.favorites

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.butul0ve.urbanslang.R
import com.butul0ve.urbanslang.UrbanSlangApp
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.FragmentCallback
import com.butul0ve.urbanslang.utils.hideKeyboard
import javax.inject.Inject

private const val QUERY = "query_extra_key"

class FavoritesFragment : Fragment(), FavoritesMvpView {

    @Inject
    lateinit var presenter: FavoritesMvpPresenter<FavoritesMvpView>

    private lateinit var toolbar: Toolbar
    private lateinit var menuToolbarIcon: ImageView
    private lateinit var definitionsRV: RecyclerView
    private lateinit var noResultTV: TextView
    private lateinit var searchView: SearchView
    private lateinit var deleteFAB: FloatingActionButton

    private lateinit var callback: FragmentCallback
    private lateinit var query: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        UrbanSlangApp.netComponent.inject(this)
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
        searchView = view.findViewById(R.id.search_view)
        menuToolbarIcon = view.findViewById(R.id.toolbar_icon)
        definitionsRV = view.findViewById(R.id.definitions_RV)
        noResultTV = view.findViewById(R.id.no_results_TV)
        deleteFAB = view.findViewById(R.id.delete_FAB)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        definitionsRV.setOnTouchListener { v, event ->
            definitionsRV.hideKeyboard(activity!!)
            false
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(QUERY)) {
            query = savedInstanceState.getString(QUERY)!!
        }

        menuToolbarIcon.setOnClickListener { callback.onMenuToolbarClick() }
        deleteFAB.show()
        deleteFAB.setOnClickListener { showSnackbarClearFavorites() }
        initSearchView()
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        if (searchView.query.toString().isNotEmpty()) {
            query = searchView.query.toString()
            presenter.filterFavoritesDefinitions(query)
        } else {
            presenter.loadAllFavoritesDefinitions()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (searchView.query != null) {
            outState.putString(QUERY, searchView.query.toString())
        }
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

    override fun showSuccessSnackbar() {
        Snackbar.make(deleteFAB, getString(R.string.success), Snackbar.LENGTH_SHORT).show()
        if (::presenter.isInitialized) {
            presenter.loadAllFavoritesDefinitions()
        }
    }

    private fun initSearchView() {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
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
                            presenter.loadAllFavoritesDefinitions()
                        } else {
                            presenter.filterFavoritesDefinitions(it)
                        }
                    }
                }
                return true
            }

        })
        searchView.onActionViewExpanded()
        activity?.applicationContext?.let { searchView.hideKeyboard(it) }
        searchView.clearFocus()
    }

    private fun showSnackbarClearFavorites() {
        Snackbar.make(deleteFAB, getText(R.string.delete_question), Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.yes) {
                presenter.clearFavorites()
            }.show()
    }
}