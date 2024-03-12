package com.example.enuyguncase.ui.checkout

import androidx.lifecycle.ViewModel
import com.example.enuyguncase.domain.model.PaymentDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutFragmentViewModel @Inject constructor(
) : ViewModel() {

   var paymentDetail: PaymentDetail? = null
}