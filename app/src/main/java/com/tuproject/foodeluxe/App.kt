package com.tuproject.foodeluxe

import com.tuproject.foodeluxe.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins


class App: DaggerApplication() {

    /*companion object {
        lateinit var recipeComponent: RecipeComponent
    }*/

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {} // nothing or some logging
        /*recipeComponent = DaggerRecipeComponent.builder()
            .appModule(AppModule(this))
            .build()*/
    }
}