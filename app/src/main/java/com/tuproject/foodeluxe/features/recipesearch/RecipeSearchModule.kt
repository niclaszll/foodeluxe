package com.tuproject.foodeluxe.features.recipesearch

import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.di.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RecipeSearchModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun recipeSearchFragment(): RecipeSearchFragment

    @ActivityScoped
    @Binds
    abstract fun recipeSearchPrenseter(presenter: RecipeSearchPresenter): RecipeSearchContract.Presenter
}