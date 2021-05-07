package com.butul0ve.urbanslang.mvvm.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.butul0ve.urbanslang.UrbanSlangApp
import com.butul0ve.urbanslang.data.DataManager
import com.butul0ve.urbanslang.databinding.FragmentMainNewBinding
import com.butul0ve.urbanslang.mvvm.factory.ViewModelFactory
import javax.inject.Inject

private const val QUERY_EXTRA = "query_extra_key"

class MainFragment: Fragment() {

    private var binding: FragmentMainNewBinding? = null
    private val viewModel: MainViewModel by viewModels { ViewModelFactory(dataManager) }

    @Inject
    lateinit var dataManager: DataManager

    private var query: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UrbanSlangApp.netComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentMainNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        query = savedInstanceState?.getString(QUERY_EXTRA, "") ?: ""
        binding?.let { prepareUI(it) }
        viewModel.getData(query)
        viewModel.definitionAdapter.observe(viewLifecycleOwner, { adapter ->
            binding?.definitionsRV?.adapter = adapter
        })

        viewModel.onClick.observe(viewLifecycleOwner, { event ->
            val id = event.getContentIfNotHandledOrReturnNull()
            id?.let {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(it)
                findNavController().navigate(action)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        query = binding?.toolbar?.searchView?.query?.toString() ?: ""
        outState.putString(QUERY_EXTRA, query)
    }

    private fun prepareUI(binding: FragmentMainNewBinding) {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.toolbar.searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}