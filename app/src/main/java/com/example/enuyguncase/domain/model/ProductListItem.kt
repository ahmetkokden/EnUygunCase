package com.example.enuyguncase.domain.model

data class ProductListItem(
    val id:Long?,
    val title:String?,
    val description:String?,
    val displayPrice:String?,
    val price:Double?,
    val displayFinalPrice:String?,
    val finalPrice:Double?,
    var isFavorited:Boolean = false,
    val discountPercentage: Double?,
    val stock: Int?,
    val brand: String?,
    val category: String?,
    val thumbnail: String?,
    val productImages:List<String>?
)
