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
import com.example.enuyguncase.domain.mapper.toPaymentProduct
import com.example.enuyguncase.domain.model.PaymentDetail
import com.example.enuyguncase.navigation.setBadgeNumber
import com.example.enuyguncase.ui.checkout.CheckoutFragment.Companion.CHECKOUT_MODEL
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
                val bundle = setCheckoutBundle()
                findNavController().navigate(
                    R.id.action_basket_fragment_to_checkout_fragment,
                    bundle
                )
            }
        }
    }

    private fun setCheckoutBundle(): Bundle {
        val paymentProductList = basketFragmentViewModel.product.replayCache.last()?.map {
            it.toPaymentProduct()
        }

        val paymentDetail = PaymentDetail(
            productList = paymentProductList ?: emptyList(),
            buyerMail = "",
            buyerName = "",
            buyerNo = "",
            totalDiscount = basketFragmentViewModel.totalDiscount,
            totalPrice = basketFragmentViewModel.totalPrice,
            totalFinalPrice = basketFragmentViewModel.totalFinalPrice
        )

        return Bundle().apply {
            putParcelable(CHECKOUT_MODEL, paymentDetail)
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
        var totalPrice = 0.0

        var totalFinalPrice = 0.0

        var totalDiscount = 0.0

        if (listEntity.isEmpty().not()) {
            totalPrice =
                listEntity.map { it.price * it.productCount }.reduce { sum, price -> sum + price }

            totalFinalPrice = listEntity.map { it.final_price * it.productCount }
                .reduce { sum, price -> sum + price }

            totalDiscount = totalPrice - totalFinalPrice
        }

        basketFragmentViewModel.totalFinalPrice = totalFinalPrice
        basketFragmentViewModel.totalDiscount = totalDiscount
        basketFragmentViewModel.totalPrice = totalPrice

        with(binding) {
            tvPrice.text = context?.getString(R.string.price, totalPrice.format())
            tvTotalPrice.text = context?.getString(R.string.price, totalFinalPrice.format())
            tvDiscount.text = context?.getString(R.string.price, totalDiscount.format())
            tvCheckoutButton.isEnabled = listEntity.isEmpty().not()
        }

    }
}