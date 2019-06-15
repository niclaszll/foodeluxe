package com.tuproject.foodeluxe.data.api

import com.tuproject.foodeluxe.BuildConfig
import retrofit2.Call
import javax.inject.Inject

//wrapper for edamam API
class RecipeRestAPI @Inject constructor(private val edamamAPI: EdamamAPI): RecipeAPI {

    override fun getRecipes(q: String, from: Int): Call<EdamamResponse> {
        return edamamAPI.getRecipeSearchResults(q, BuildConfig.EDAMAM_APP_ID, BuildConfig.EDAMAM_APP_KEY, from)
    }
}