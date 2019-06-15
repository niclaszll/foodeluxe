package com.tuproject.foodeluxe.features.startpage

import android.net.ConnectivityManager
import android.support.annotation.Nullable
import android.util.Log
import com.tuproject.foodeluxe.data.Config
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.data.RecipeSearchResults
import com.tuproject.foodeluxe.data.api.RecipeAPI
import com.tuproject.foodeluxe.data.local.RecipeDatabase
import com.tuproject.foodeluxe.di.ActivityScoped
import io.reactivex.Observable
import javax.inject.Inject

@ActivityScoped
class StartpagePresenter @Inject constructor(private val apiRecipe: RecipeAPI, var recipeDatabase: RecipeDatabase, private val connectivityManager: ConnectivityManager): StartpageContract.Presenter {

    @Nullable
    private var startpageFragment: StartpageContract.View? = null

    var from: Int = 0

    override fun getRecipes(q: String): Observable<RecipeSearchResults> {
        return Observable.create {

                subscriber ->

            val callResponse = apiRecipe.getRecipes(q, from)

            //increase "from" after each call by "newItemsCount" to get the next results
            from += getItemReloadCount()

            val response = callResponse.execute()

            if (response.isSuccessful) {
                val recipes = response.body()!!.hits.map {
                    val item = it.recipe
                    RecipeItem(
                        item.label,
                        item.image,
                        item.source,
                        item.calories,
                        item.totalTime,
                        item.yield,
                        item.dietLabels,
                        item.healthLabels,
                        item.uri,
                        item.url,
                        item.cautions,
                        item.ingredientLines,
                        item.totalWeight,
                        false
                    )
                }
                val results = RecipeSearchResults(
                    response.body()!!.q,
                    recipes
                )

                subscriber.onNext(results)
                subscriber.onComplete()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    override fun takeView(view: StartpageContract.View) {
        this.startpageFragment = view
    }

    override fun dropView() {
        startpageFragment = null
    }

    override fun saveItemToRecipeDatabase(item: RecipeItem) {
        recipeDatabase.recipeDao().insert(item)
    }

    private fun getItemReloadCount(): Int {
        var count = 0
        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network).apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    count = 10

                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    count = 5
                }
            }
        }
        Log.v("StartpagePresenter", count.toString())
        return count
    }

}