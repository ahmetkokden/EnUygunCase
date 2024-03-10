package com.example.enuyguncase.domain.repository

import com.example.enuyguncase.data.ProductService
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {
}