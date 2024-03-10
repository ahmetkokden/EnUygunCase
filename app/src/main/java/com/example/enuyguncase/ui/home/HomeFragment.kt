package com.example.enuyguncase.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.FragmentHomeBinding
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeData()
        homeViewModel.getProductList()
    }

    private fun initUI() {
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
            productList = productList.toMutableList()
        )
        binding.rvHomeProduct.adapter = adapter
    }
}