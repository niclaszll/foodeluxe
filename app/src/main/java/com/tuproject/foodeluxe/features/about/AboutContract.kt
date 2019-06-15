package com.tuproject.foodeluxe.features.about

import com.tuproject.BasePresenter
import com.tuproject.BaseView
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.data.RecipeSearchResults
import io.reactivex.Observable


interface AboutContract {

    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter<View> {

        override fun takeView(view: AboutContract.View)
        override fun dropView()
    }
}