package com.example.enuyguncase.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.enuyguncase.R
import com.example.enuyguncase.data.local.BasketProductListEntity
import com.example.enuyguncase.databinding.FragmentPaymentBinding
import com.example.enuyguncase.domain.model.PaymentDetail
import com.example.enuyguncase.domain.model.PaymentProduct
import com.example.enuyguncase.ui.checkout.CheckoutFragment
import com.example.enuyguncase.utilities.format
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding

    private val basketFragmentViewModel: PaymentFragmentViewModel by viewModels()

    private lateinit var adapter: PaymentProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeData()
    }

    private fun initUI() {
        with(binding) {
            tvPaymentCompleteButton.setOnClickListener {
            }

            ivLeftIcon.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeData() {
        val paymentDetail: PaymentDetail? = requireArguments().getParcelable(
            PAYMENT_MODEL
        )

        paymentDetail?.let {
            prepareProductList(it.productList)
            setPaymentComponent(it)
        }

    }


    private fun prepareProductList(productList: List<PaymentProduct>) {
        adapter = PaymentProductListAdapter(
            productList = productList.toMutableList(),
        )
        binding.rvPaymentProduct.adapter = adapter
    }

    private fun setPaymentComponent(paymentDetail: PaymentDetail) {
        with(binding) {
            tvPrice.text = context?.getString(R.string.price, paymentDetail.totalPrice.format())
            tvTotalPrice.text = context?.getString(R.string.price, paymentDetail.totalFinalPrice.format())
            tvDiscount.text = context?.getString(R.string.price, paymentDetail.totalDiscount.format())
            tvBuyerMail.text = paymentDetail.buyerMail
            tvBuyerName.text = paymentDetail.buyerName
            tvBuyerNumber.text = paymentDetail.buyerNo
        }

    }

    companion object {
        const val PAYMENT_MODEL = "paymentModel"
    }
}