package com.example.enuyguncase.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDetail(
    var productList:List<PaymentProduct>,
    var buyerName: String,
    var buyerNo: String,
    var buyerMail: String,
    var totalPrice: Double,
    var totalFinalPrice: Double,
    var totalDiscount: Double,
) : Parcelable
