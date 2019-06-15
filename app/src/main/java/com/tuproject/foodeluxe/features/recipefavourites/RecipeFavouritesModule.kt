package com.tuproject.foodeluxe.features.recipefavourites

import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.di.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class RecipeFavouritesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun recipeFavouritesFragment(): RecipeFavouritesFragment

    @ActivityScoped
    @Binds
    abstract fun recipeFavouritesPresenter(presenter: RecipeFavouritesPresenter): RecipeFavouritesContract.Presenter
}