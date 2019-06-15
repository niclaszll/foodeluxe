package com.tuproject.foodeluxe.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.tuproject.foodeluxe.data.ListConverter
import com.tuproject.foodeluxe.data.RecipeItem

@Database(entities = [RecipeItem::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}