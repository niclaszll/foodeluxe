package com.tuproject.foodeluxe.features.startpage

import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.di.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class StartpageModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun startpageFragment(): StartpageFragment

    @ActivityScoped
    @Binds
    abstract fun startpagePresenter(presenter: StartpagePresenter): StartpageContract.Presenter
}