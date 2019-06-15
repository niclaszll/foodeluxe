package com.tuproject.foodeluxe.di.recipes

import android.arch.persistence.room.Room
import android.content.Context
import com.tuproject.foodeluxe.data.local.RecipeDao
import com.tuproject.foodeluxe.data.local.RecipeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesRecipeDatabase(context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "recipe-database"
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun providesRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao()
    }
}