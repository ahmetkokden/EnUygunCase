package com.example.enuyguncase.data.remote.response

import java.io.Serializable

data class ProductListResponse(
    val products: List<ProductResponse>?,
    val total: Int?,
    val skip: Int?,
    val limit: Int?
) : Serializable
