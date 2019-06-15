package com.tuproject.foodeluxe.di

import com.tuproject.foodeluxe.MainActivity
import com.tuproject.foodeluxe.features.about.AboutModule
import com.tuproject.foodeluxe.features.recipedetail.RecipeDetailActivity
import com.tuproject.foodeluxe.features.recipedetail.RecipeDetailModule
import com.tuproject.foodeluxe.features.recipefavourites.RecipeFavouritesModule
import com.tuproject.foodeluxe.features.recipesearch.RecipeSearchModule
import com.tuproject.foodeluxe.features.startpage.StartpageModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(RecipeSearchModule::class), (StartpageModule::class), (RecipeFavouritesModule::class), (AboutModule::class)])
    abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(RecipeDetailModule::class)])
    abstract fun detailActivity(): RecipeDetailActivity

}