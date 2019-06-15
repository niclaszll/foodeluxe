package com.tuproject.foodeluxe.features.recipedetail

import android.content.Intent
import android.net.Uri
import android.support.annotation.Nullable
import android.util.Log
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.data.local.RecipeDatabase
import com.tuproject.foodeluxe.di.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RecipeDetailPresenter @Inject constructor(var recipeDatabase: RecipeDatabase) : RecipeDetailContract.Presenter {

    @Nullable
    private var recipeDetailFragment: RecipeDetailContract.View? = null

    override fun takeView(view: RecipeDetailContract.View) {
        this.recipeDetailFragment = view
    }

    override fun dropView() {
        recipeDetailFragment = null
    }

    override fun saveItemToRecipeDatabase(item: RecipeItem) {
        recipeDatabase.recipeDao().insert(item)
        Log.v("DetailPresenter", "item added")
    }

    override fun removeItemFromRecipeDatabase(item: RecipeItem) {
        recipeDatabase.recipeDao().delete(item)
    }
}