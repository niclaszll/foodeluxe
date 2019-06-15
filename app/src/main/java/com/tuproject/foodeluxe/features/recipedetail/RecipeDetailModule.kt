package com.tuproject.foodeluxe.features.recipedetail

import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.di.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class RecipeDetailModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun recipeDetailFragment(): RecipeDetailFragment

    @ActivityScoped
    @Binds
    abstract fun recipeDetailPresenter(presenter: RecipeDetailPresenter): RecipeDetailContract.Presenter
}