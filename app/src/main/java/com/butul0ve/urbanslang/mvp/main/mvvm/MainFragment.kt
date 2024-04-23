package com.butul0ve.urbanslang.mvp.main.mvvm

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.butul0ve.urbanslang.UrbanSlangApp
import com.butul0ve.urbanslang.adapter.DefinitionAdapter
import com.butul0ve.urbanslang.adapter.DefinitionClickListener
import com.butul0ve.urbanslang.data.DataRepo
import com.butul0ve.urbanslang.databinding.FragmentMainBinding
import com.butul0ve.urbanslang.utils.hideKeyboard
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment(), DefinitionClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataRepo: DataRepo

    private lateinit var viewModel: MainViewModel

    private var query: String? = null

    private var adapter: DefinitionAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UrbanSlangApp.netComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbarRoot)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
//        menuToolbarIcon.setOnClickListener { callback.onMenuToolbarClick() }

        viewModel = ViewModelProvider(
            this,
            MainViewModel.Companion.MainViewModelFactory(dataRepo)
        )[MainViewModel::class.java]
        initSearchView()
        binding.definitionsRV.setOnTouchListener { v, event ->
            binding.definitionsRV.hideKeyboard(requireActivity())
            false
        }
        query = savedInstanceState?.getString(QUERY) ?: ""
        viewModel.onAction(MainViewModel.Action.GetData(query!!))
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is MainViewModel.State.Data -> {
                            if (adapter == null) {
                                adapter = DefinitionAdapter(state.definitions, this@MainFragment)
                            } else {
                                adapter!!.updateDefinitions(state.definitions)
                            }
                            showResultSearch(adapter!!)
                        }
                        MainViewModel.State.Error -> showError()
                        MainViewModel.State.Loading -> showProgressbar()
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val searchView = binding.toolbar.searchView
        if (searchView.query != null) {
            outState.putString(QUERY, searchView.query.toString())
        }
    }

    override fun onItemClick(position: Int) {
        val definitionId = adapter?.definitions?.get(position)?.id?: return
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(definitionId)
        findNavController().navigate(action)
    }

    private fun initSearchView() {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.toolbar.searchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        if (query?.isNotBlank() == true) {
            searchView.isIconified = false
            searchView.setQuery(query, false)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                viewModel.onAction(MainViewModel.Action.SetQuery(text))
                context?.let { searchView.hideKeyboard(it) }

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

    private fun showResultSearch(adapter: DefinitionAdapter) {
        binding.definitionsRV.isVisible = true
        binding.noResultsTV.isGone = true
        binding.definitionsRV.adapter = adapter
        binding.progressBar.isGone = true
        adapter.notifyDataSetChanged()
    }

    private fun showError() {
        binding.definitionsRV.isGone = true
        binding.noResultsTV.isVisible = true
        binding.progressBar.isGone = true
    }

    private fun showProgressbar() {
        binding.definitionsRV.isGone = true
        binding.noResultsTV.isGone = true
        binding.progressBar.isVisible = true
    }

    companion object {

        private const val QUERY = "query_extra_key"

        fun newInstance(query: String? = null): MainFragment {
            val fragment = MainFragment()
            query?.let { param ->
                fragment.arguments = Bundle(1).apply {
                    putString(QUERY, param)
                }
            }
            return fragment
        }
    }
}