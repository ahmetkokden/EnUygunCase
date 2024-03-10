package com.example.enuyguncase.ui.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.FragmentProductDetailBinding
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.ui.home.HomeFragment.Companion.PRODUCT_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val productDetailFragmentViewModel: ProductDetailFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeData()
    }

    private fun initUI() {
        val productId = requireArguments().getInt(PRODUCT_ID)
        productDetailFragmentViewModel.getProductDetail(productId)
        with(binding) {
            ivRightIcon.setOnClickListener {
                productDetailFragmentViewModel.setProductFavoritedStatus(
                    productDetailFragmentViewModel.product.replayCache.last()?.isFavorited?.not() ?: false
                )
            }
            ivLeftIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            tvAddCartButton.setOnClickListener {
                productDetailFragmentViewModel.addCartFavoritedProduct()
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            productDetailFragmentViewModel.product.collect {
                it?.let {
                    setProductDetailComponents(it)
                }
            }
        }
    }

    private fun setProductDetailComponents(product: ProductListItem) {
        with(binding) {
            if (product.isFavorited) {
                ivRightIcon.setImageResource(R.drawable.ic_favorite_selected_red)
            } else {
                ivRightIcon.setImageResource(R.drawable.ic_favorite)
            }
            binding.tvProductTitle.text = product.title
        }
    }
}