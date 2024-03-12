package com.example.enuyguncase.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentProduct(
    var title: String,
    var finalPrice: Double,
    var productCount: Int,
    var thumbnail: String,
):Parcelable
