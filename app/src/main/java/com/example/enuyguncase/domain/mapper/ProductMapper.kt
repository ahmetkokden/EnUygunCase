package com.example.enuyguncase.domain.mapper

import com.example.enuyguncase.data.remote.response.ProductListResponse
import com.example.enuyguncase.data.remote.response.ProductResponse
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.utilities.format

fun ProductResponse.toProductListItem() = ProductListItem(
    id = id,
    title = title,
    description = description,
    price = price,
    displayPrice = price?.format().toString(),
    finalPrice = (price?.minus(price.times((discountPercentage ?: 0.0)/100))),
    displayFinalPrice = (price?.minus(price.times((discountPercentage ?: 0.0)/100)))?.format(),
    isFavorited = false,
    discountPercentage = discountPercentage,
    stock = stock,
    brand = brand,
    category = category,
    thumbnail = thumbnail,
    productImages = images,
)

fun ProductListResponse.toProductList() = ProductList(
    products = products?.map {
        it.toProductListItem()
    } ?: emptyList(),
    totalCount = total ?: 0
)