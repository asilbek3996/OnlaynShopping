package com.example.onlaynshop.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.onlaynshop.model.CategoryModel
import com.example.onlaynshop.model.ProductModel


@Dao
interface ProductDao {

    @Query("DELETE from products")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items:List<ProductModel>)

    @Query("SELECT * FROM products")
    fun getAllProducts():List<ProductModel>


}