package com.tuproject.foodeluxe.features.recipedetail

import com.tuproject.BasePresenter
import com.tuproject.BaseView
import com.tuproject.foodeluxe.data.RecipeItem


interface RecipeDetailContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter<View> {

        override fun takeView(view: RecipeDetailContract.View)
        override fun dropView()
        fun saveItemToRecipeDatabase(item: RecipeItem)
        fun removeItemFromRecipeDatabase(item: RecipeItem)
    }
}