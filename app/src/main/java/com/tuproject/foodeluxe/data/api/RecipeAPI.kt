package com.tuproject.foodeluxe.data.api

import retrofit2.Call


interface RecipeAPI {
    fun getRecipes(q: String, from: Int): Call<EdamamResponse>
}