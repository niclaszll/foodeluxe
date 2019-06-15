package com.tuproject.foodeluxe.data.api

class EdamamResponse(
    val q: String,
    //val from: Int,
    //val to: Int,
    val hits: List<EdamamHitsResponse>
)

class EdamamHitsResponse(
    val recipe: RecipeResponse
)

class RecipeResponse(
    val label: String,
    val image: String,
    val source: String,
    val calories: Float,
    val totalTime: Int,
    val yield: Int,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val uri: String,
    val url: String,
    val cautions: List<String>,
    val ingredientLines: List<String>,
    val totalWeight: Float
    //val ingredients: List<RecipeIngredientResponse>,
    //val totalNutrients: List<RecipeNutrientResponse>,
    //val totalDaily: List<RecipeNutrientResponse>


)

class RecipeIngredientResponse(
    val text: String,
    val weight: Float,
    val foodCategory: String
)