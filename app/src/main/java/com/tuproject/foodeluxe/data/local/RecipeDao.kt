package com.tuproject.foodeluxe.data.local

import android.arch.persistence.room.*
import com.tuproject.foodeluxe.data.RecipeItem

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipeitem")
    fun getAll(): List<RecipeItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeItem)

    @Delete
    fun delete(recipe: RecipeItem)
}