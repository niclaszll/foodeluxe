package com.tuproject.foodeluxe.di.recipes

import com.tuproject.foodeluxe.data.api.EdamamAPI
import com.tuproject.foodeluxe.data.api.RecipeAPI
import com.tuproject.foodeluxe.data.api.RecipeRestAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class RecipeApiModule {

    @Provides
    @Singleton
    fun provideRecipeAPI(edamamAPI: EdamamAPI): RecipeAPI {
        return RecipeRestAPI(edamamAPI)
    }

    @Provides
    @Singleton
    fun provideEdamamAPI(retrofit: Retrofit): EdamamAPI {
        return retrofit.create(EdamamAPI::class.java)
    }
}