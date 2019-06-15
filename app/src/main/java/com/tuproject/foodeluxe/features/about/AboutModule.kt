package com.tuproject.foodeluxe.features.about

import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.di.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AboutModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun aboutFragment(): AboutFragment

    @ActivityScoped
    @Binds
    abstract fun aboutPresenter(presenter: AboutPresenter): AboutContract.Presenter
}