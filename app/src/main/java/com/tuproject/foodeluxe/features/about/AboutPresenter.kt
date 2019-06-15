package com.tuproject.foodeluxe.features.about

import android.support.annotation.Nullable
import com.tuproject.foodeluxe.data.Config
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.data.RecipeSearchResults
import com.tuproject.foodeluxe.data.api.RecipeAPI
import com.tuproject.foodeluxe.data.local.RecipeDatabase
import com.tuproject.foodeluxe.di.ActivityScoped
import io.reactivex.Observable
import javax.inject.Inject

@ActivityScoped
class AboutPresenter @Inject constructor(): AboutContract.Presenter {

    @Nullable
    private var startpageFragment: AboutContract.View? = null

    override fun takeView(view: AboutContract.View) {
        this.startpageFragment = view
    }

    override fun dropView() {
        startpageFragment = null
    }
}