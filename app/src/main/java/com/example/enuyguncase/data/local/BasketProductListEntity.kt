package com.example.enuyguncase.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(
    tableName = "basket_list_table"
)
data class BasketProductListEntity (
    @PrimaryKey()
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "final_price")
    val profileImage: Double,
    @ColumnInfo(name = "stock")
    val userType: Int,
    @ColumnInfo(name = "product_count")
    val productCount: Int,
    @ColumnInfo(name = "thumbnail")
    val userUrl: String
)