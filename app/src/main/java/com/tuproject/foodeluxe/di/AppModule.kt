package com.tuproject.foodeluxe.di

import android.app.Application
import android.content.Context
import com.tuproject.foodeluxe.App
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
abstract class AppModule() {

    //val app: App in constructor

    //expose Application as an injectable context
    @Binds
    abstract fun bindContext(application: Application): Context

/*    @Provides
    @Singleton
    fun provideApplication(): App {
        return app
    }*/
}