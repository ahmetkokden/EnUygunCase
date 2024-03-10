package com.example.enuyguncase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.enuyguncase.data.local.BasketProductListEntity
import com.example.enuyguncase.data.local.FavoriteProductListEntity

@Database(
    entities = [BasketProductListEntity::class, FavoriteProductListEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase(){

    abstract fun basketProductDao(): BasketProductDao

    abstract fun favoriteProductDao(): FavoriteProductDao
}