package com.example.enuyguncase.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("discountPercentage") val discountPercentage: Double?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("stock") val stock: Int?,
    @SerializedName("brand") val brand: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("images")  val images: List<String>?
)
