package com.example.enuyguncase.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.enuyguncase.R
import com.example.enuyguncase.data.local.FavoriteProductListEntity
import com.example.enuyguncase.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    private val favoritedFragmentViewModel: FavoritedFragmentViewModel by viewModels()

    private lateinit var adapter: FavoriteProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeData()
    }

    private fun initUI() {
        favoritedFragmentViewModel.getFavoritedProduct()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            favoritedFragmentViewModel.product.collect {
                if (it != null) {
                    prepareProductList(it)
                }
            }
        }
    }

    private fun prepareProductList(productList: List<FavoriteProductListEntity>) {
        adapter = FavoriteProductListAdapter(
            productList = productList.toMutableList(),
            unFavoritedAction = {
                favoritedFragmentViewModel.unFavoritedProduct(it.toLong())
            },
            addedBasket = {
                favoritedFragmentViewModel.addCartFavoritedProduct(it)
            }
        )
        binding.rvFavoriteProduct.adapter = adapter
    }
}