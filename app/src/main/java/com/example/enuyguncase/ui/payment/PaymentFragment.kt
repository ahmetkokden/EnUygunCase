package com.example.enuyguncase.ui.payment

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
import com.example.enuyguncase.databinding.FragmentPaymentBinding
import com.example.enuyguncase.domain.model.PaymentDetail
import com.example.enuyguncase.domain.model.PaymentProduct
import com.example.enuyguncase.navigation.MultiNavHost
import com.example.enuyguncase.utilities.format
import com.example.enuyguncase.utilities.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding

    private val paymentFragmentViewModel: PaymentFragmentViewModel by viewModels()

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
        MultiNavHost.bottomNavigationListener.bottomNavVisibility(false)
        initUI()
        observeData()
    }

    private fun initUI() {
        with(binding) {
            tvPaymentCompleteButton.setOnClickListener {
                if (controlFields()) {
                    /*
                    showAlertDialog(
                        false,
                        title = "HATA",
                        message = "Ödemeniz Başarısız Oldu",
                        onFinishListener = {
                            if (it) {
                                paymentFragmentViewModel.deleteAllBasketProduct()
                                findNavController().navigate(R.id.basket_fragment)
                            }
                        })

                     */
                    showAlertDialog(
                        true,
                        title = "Başarılı Ödeme",
                        message = "Ödemeniz Başarılı Sepet Ekranına Yönlendiriliyosunuz...",
                        onFinishListener = {
                            if (it) {
                                findNavController().popBackStack(R.id.basket_fragment,false)
                            }
                        })
                }
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
            tvTotalPrice.text =
                context?.getString(R.string.price, paymentDetail.totalFinalPrice.format())
            tvDiscount.text =
                context?.getString(R.string.price, paymentDetail.totalDiscount.format())
            tvBuyerMail.text = paymentDetail.buyerMail
            tvBuyerName.text = paymentDetail.buyerName
            tvBuyerNumber.text = paymentDetail.buyerNo
        }

    }

    private fun controlFields(): Boolean {
        return (binding.etCardName.text.isNotEmpty() && binding.etCardNo.text.isNotEmpty() && binding.etCardDate.text.isNotEmpty())
    }

    companion object {
        const val PAYMENT_MODEL = "paymentModel"
    }
}