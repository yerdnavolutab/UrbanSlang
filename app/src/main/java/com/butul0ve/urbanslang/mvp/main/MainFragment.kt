package com.butul0ve.urbanslang.mvp.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.butul0ve.urbanslang.R
import com.butul0ve.urbanslang.UrbanSlangApp
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.utils.hideKeyboard
import javax.inject.Inject

private const val QUERY = "query_extra_key"
private const val RANDOM = "is_random_key"
private const val WORD = "word_extra_key"

class MainFragment : Fragment(), MainMvpView {

    @Inject
    lateinit var presenter: MainMvpPresenter<MainMvpView>

    private lateinit var toolbar: Toolbar
    private lateinit var menuToolbarIcon: ImageView
    private lateinit var definitionsRV: RecyclerView
    private lateinit var noResultTV: TextView
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar

    private lateinit var query: String
    private lateinit var word: String
    private var isRandom = false

//    private val args: MainFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UrbanSlangApp.netComponent.inject(this)
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
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
//        menuToolbarIcon.setOnClickListener { callback.onMenuToolbarClick() }
        initSearchView()
        definitionsRV.setOnTouchListener { v, event ->
            definitionsRV.hideKeyboard(requireActivity())
            false
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(QUERY)) {
            query = savedInstanceState.getString(QUERY)!!
        }

//        isRandom = args.isRandom

        if (arguments != null) {

            if (arguments!!.containsKey(WORD)) {
                word = arguments!!.getString(WORD)!!
            }

            arguments = null
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        if (isRandom) {
            presenter.getData()
            isRandom = false
        } else if (::word.isInitialized) {
            presenter.getData(word)
        }
    }

    override fun onStop() {
        super.onStop()
        if (::presenter.isInitialized) {
            presenter.onDetach()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (::searchView.isInitialized && searchView.query != null) {
            outState.putString(QUERY, searchView.query.toString())
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
            override fun onQueryTextSubmit(text: String): Boolean {
                if (::presenter.isInitialized) {
                    presenter.getData(text)
                    activity?.applicationContext?.let { searchView.hideKeyboard(it) }
                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        searchView.onActionViewExpanded()
        activity?.applicationContext?.let { searchView.hideKeyboard(it) }
        searchView.clearFocus()
    }

    override fun showResultSearch(adapter: DefinitionAdapter) {
        definitionsRV.visibility = View.VISIBLE
        noResultTV.visibility = View.GONE
        adapter.notifyDataSetChanged()
        definitionsRV.adapter = adapter
        progressBar.visibility = View.GONE
    }

    override fun showError() {
        definitionsRV.visibility = View.GONE
        noResultTV.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onClick(definition: Definition) {
//        definition.id?.let { findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(it)) }
    }

    override fun showProgressbar() {
        definitionsRV.visibility = View.GONE
        noResultTV.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    companion object {

        fun newInstance(word: String): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString(WORD, word)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(isRandom: Boolean): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putBoolean(RANDOM, isRandom)
            fragment.arguments = args
            return fragment
        }
    }
}