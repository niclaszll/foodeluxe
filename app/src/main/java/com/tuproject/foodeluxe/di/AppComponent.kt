package com.tuproject.foodeluxe.di

import android.app.Application
import com.tuproject.foodeluxe.App
import com.tuproject.foodeluxe.di.recipes.RecipeApiModule
import com.tuproject.foodeluxe.di.recipes.RoomModule
import com.tuproject.foodeluxe.features.recipedetail.RecipeDetailModule
import com.tuproject.foodeluxe.features.recipesearch.RecipeSearchModule
import com.tuproject.foodeluxe.features.startpage.StartpageModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules =
    [AppModule::class,
        ActivityBindingModule::class,
        AndroidSupportInjectionModule::class,
        RecipeApiModule::class,
        NetworkModule::class,
        RoomModule::class]
)
interface AppComponent : AndroidInjector<App> {

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}