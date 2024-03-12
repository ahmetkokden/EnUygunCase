package com.example.enuyguncase.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.FragmentHomeBinding
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.navigation.MultiNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeFragmentViewModel by viewModels()

    private lateinit var adapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MultiNavHost.bottomNavigationListener.bottomNavVisibility(true)
        initUI()
        observeData()
        homeViewModel.getProductList()
        homeViewModel.getProductCategories()
    }

    private fun initUI() {
        with(binding) {
            tvSortButton.setOnClickListener {
                SortBottomSheetFragment.newInstance(selectOnClickListener = {
                    homeViewModel.sortProductAboutEvent(it)
                }).show(childFragmentManager, SortBottomSheetFragment::javaClass.name)
            }
            tvFilterButton.setOnClickListener {
                FilterDialogFragment.newInstance(setApplyClickListener = {
                    homeViewModel.filterProduct(it)
                },homeViewModel.productCategories.value
                ).show(childFragmentManager, FilterDialogFragment::javaClass.name)
            }

            srlProductList.setOnRefreshListener {
                homeViewModel.getProductList()
                srlProductList.isRefreshing = false
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.productList.collect {
                setTotalCount()
                if (it.isEmpty().not())
                    prepareProductList(it)
            }
        }
    }

    private fun setTotalCount() {
        with(binding) {
            tvProductCount.text =
                context?.getString(R.string.product_count, homeViewModel.productList.value.size)
        }
    }

    private fun prepareProductList(productList: List<ProductListItem>) {
        adapter = ProductListAdapter(
            productList = productList.toMutableList(),
            clickListener = {
                productClicked(it)
            }
        )
        binding.rvHomeProduct.adapter = adapter
    }

    private fun productClicked(productId:Int) {
        binding.apply {
            val bundle =
                bundleOf(PRODUCT_ID to productId)
            findNavController().navigate(
                R.id.action_home_fragment_to_product_detail_fragment, bundle
            )
        }
    }

    companion object {
        const val PRODUCT_ID = "productID"
    }
}