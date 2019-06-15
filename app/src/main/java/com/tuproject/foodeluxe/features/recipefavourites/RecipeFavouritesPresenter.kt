package com.tuproject.foodeluxe.features.recipefavourites

import android.support.annotation.Nullable
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.data.local.RecipeDatabase
import com.tuproject.foodeluxe.di.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RecipeFavouritesPresenter @Inject constructor(private var recipeDatabase: RecipeDatabase): RecipeFavouritesContract.Presenter {

    @Nullable
    private var startpageFragment: RecipeFavouritesContract.View? = null

    override fun takeView(view: RecipeFavouritesContract.View) {
        this.startpageFragment = view
    }

    override fun dropView() {
        startpageFragment = null
    }

    override fun getFavourites(): List<RecipeItem> {
        return recipeDatabase.recipeDao().getAll()
    }

    override fun removeFromFavourites(item : RecipeItem) {
        return recipeDatabase.recipeDao().delete(item)
    }

}