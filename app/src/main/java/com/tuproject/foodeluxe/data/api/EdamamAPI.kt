package com.tuproject.foodeluxe.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface EdamamAPI {

    //get recipe results for search query
    @GET("/search")
    fun getRecipeSearchResults(
        @Query("q") q: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("from") from: Int
        //@Query("to") to: Int
        ): Call<EdamamResponse>

    //get details for one specific recipe url
    @GET("/search")
    fun getRecipeDetails(
        @Query("r") r: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String): Call<RecipeResponse>

}