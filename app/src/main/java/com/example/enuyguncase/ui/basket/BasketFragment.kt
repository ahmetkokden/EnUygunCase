package com.example.enuyguncase.ui.basket

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
import com.example.enuyguncase.data.local.BasketProductListEntity
import com.example.enuyguncase.databinding.FragmentBasketBinding
import com.example.enuyguncase.navigation.setBadgeNumber
import com.example.enuyguncase.utilities.format
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding

    private val basketFragmentViewModel: BasketFragmentViewModel by viewModels()

    private lateinit var adapter: BasketProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_basket, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeData()
    }

    private fun initUI() {
        basketFragmentViewModel.getBasketProduct()

        with(binding) {
            tvCheckoutButton.setOnClickListener {
                findNavController().navigate(R.id.action_basket_fragment_to_checkout_fragment)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            basketFragmentViewModel.product.collectLatest {
                setBadgeNumber(it?.size ?: 0)
                if (it != null) {
                    prepareProductList(it)
                    setBasketComponent(it)
                }
            }
        }
    }


    private fun prepareProductList(productList: List<BasketProductListEntity>) {
        adapter = BasketProductListAdapter(
            productList = productList.toMutableList(),
            incProduct = { count, id ->
                basketFragmentViewModel.updateProductCount(count, id)
            },
            descProduct = { count, id ->
                basketFragmentViewModel.updateProductCount(count, id)
            },
            deleteProduct = {
                basketFragmentViewModel.deleteProductFromBasket(it.toLong())
            }
        )
        binding.rvBasketProduct.adapter = adapter
    }

    private fun setBasketComponent(listEntity: List<BasketProductListEntity>) {
        val totalPrice = listEntity.map { it.price * it.productCount }.reduce { sum, price -> sum + price }

        val totalFinalPrice = listEntity.map { it.final_price * it.productCount }.reduce { sum, price -> sum + price }

        val totalDiscount = totalPrice - totalFinalPrice

        with(binding) {
            tvPrice.text = context?.getString(R.string.price, totalPrice.format())
            tvTotalPrice.text = context?.getString(R.string.price, totalFinalPrice.format())
            tvDiscount.text = context?.getString(R.string.price, totalDiscount.format())
        }

    }
}