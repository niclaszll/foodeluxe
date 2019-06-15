package com.tuproject.foodeluxe.features.recipefavourites

import com.tuproject.BasePresenter
import com.tuproject.BaseView
import com.tuproject.foodeluxe.data.RecipeItem


interface RecipeFavouritesContract {

    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter<View> {

        override fun takeView(view: RecipeFavouritesContract.View)
        override fun dropView()
        fun getFavourites(): List<RecipeItem>
        fun removeFromFavourites(item: RecipeItem)
    }
}