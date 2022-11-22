package com.example.onlaynshop.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onlaynshop.model.CategoryModel

@Dao
interface CategoryDao{

        @Query("DELETE from categories")
        fun deleteAll()

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insetAll(items:List<CategoryModel>)

        @Query("SELECT * FROM categories")
        fun getAllCategories():List<CategoryModel>
}