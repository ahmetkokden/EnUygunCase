package com.example.enuyguncase.domain.model

data class FilterResult(
    var minPrice: Int? = null,
    var maxPrice: Int? = null,
    var minDiscount: Int? = null,
)