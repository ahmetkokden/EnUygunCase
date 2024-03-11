package com.example.enuyguncase.domain.mapper

import com.example.enuyguncase.data.local.BasketProductListEntity
import com.example.enuyguncase.data.local.FavoriteProductListEntity
import com.example.enuyguncase.data.remote.response.ProductListResponse
import com.example.enuyguncase.data.remote.response.ProductResponse
import com.example.enuyguncase.domain.model.ImageItem
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.utilities.format

fun ProductResponse.toProductListItem() = ProductListItem(
    id = id,
    title = title,
    description = description,
    price = price,
    displayPrice = price?.format().toString(),
    finalPrice = (price?.minus(price.times((discountPercentage ?: 0.0) / 100))),
    displayFinalPrice = (price?.minus(price.times((discountPercentage ?: 0.0) / 100)))?.format(),
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

fun ProductListItem.toFavoriteProductListEntity() = FavoriteProductListEntity(
    id = id ?: 0,
    title = title ?: "",
    description = description ?: "",
    price = price ?: 0.0,
    stock= stock ?: 0,
    final_price = finalPrice ?: 0.0,
    thumbnail = thumbnail ?: "",
    isFavourited = isFavorited

)

fun FavoriteProductListEntity.toBasketProductListEntity() = BasketProductListEntity(
    id = id ,
    title = title ,
    price = price ,
    final_price = final_price,
    stock = stock,
    productCount = 1,
    thumbnail = thumbnail ,
)

fun ProductListItem.toImageItemList() = productImages?.map {
    ImageItem(id = id.toString(), url = it)
}

