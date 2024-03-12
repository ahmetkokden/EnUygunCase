package com.example.enuyguncase.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.FragmentCheckoutBinding
import com.example.enuyguncase.navigation.MultiNavHost
import com.example.enuyguncase.ui.payment.PaymentFragment.Companion.PAYMENT_MODEL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private val checkoutFragmentViewModel: CheckoutFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MultiNavHost.bottomNavigationListener.bottomNavVisibility(false)
        checkoutFragmentViewModel.paymentDetail = requireArguments().getParcelable(
            CHECKOUT_MODEL
        )

        initUI()
        observeData()
    }

    private fun initUI() {
        with(binding) {
            ivLeftIcon.setOnClickListener {
                findNavController().popBackStack()
            }
            tvCheckoutButton.setOnClickListener {

                if (controlFields()) {
                    val bundle = setPaymentBundle()
                    findNavController().navigate(
                        R.id.action_checkout_fragment_to_payment_fragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
        }
    }

    private fun setPaymentBundle(): Bundle {
        val paymentDetail = checkoutFragmentViewModel.paymentDetail?.apply {
            buyerMail = binding.etEmail.text.toString()
            buyerName = binding.etName.text.toString()
            buyerNo = binding.etPhone.text.toString()
        }

        return Bundle().apply {
            putParcelable(PAYMENT_MODEL, paymentDetail)
        }
    }

    private fun controlFields(): Boolean {
        return (binding.etName.text.isNotEmpty() && binding.etEmail.text.isNotEmpty() && binding.etPhone.text.isNotEmpty())
    }

    companion object {
        const val CHECKOUT_MODEL = "checkoutModel"
    }
}