package com.example.enuyguncase.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(
    tableName = "favorite_list_table"
)
data class FavoriteProductListEntity (
    @PrimaryKey()
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "stock")
    val stock: Int,
    @ColumnInfo(name = "final_price")
    val final_price: Double,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "is_favourited")
    var isFavourited: Boolean = false
)