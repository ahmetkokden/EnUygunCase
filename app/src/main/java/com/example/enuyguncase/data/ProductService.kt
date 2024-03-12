package com.example.enuyguncase.data

import com.example.enuyguncase.data.remote.response.ProductListResponse
import com.example.enuyguncase.data.remote.response.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProductList(
    ): ProductListResponse

    @GET("products/categories")
    suspend fun getProductCategories(
    ): Array<String>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): ProductResponse

    @GET("products/search")
    suspend fun searchProduct(
        @Query("q") searchKeyword: String,
    ): ProductListResponse
}