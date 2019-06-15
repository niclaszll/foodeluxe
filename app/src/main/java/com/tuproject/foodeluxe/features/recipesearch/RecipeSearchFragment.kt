package com.tuproject.foodeluxe.features.recipesearch

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.tuproject.foodeluxe.R
import com.tuproject.foodeluxe.commons.InfiniteScrollListener
import com.tuproject.foodeluxe.data.RecipeSearchResults
import com.tuproject.foodeluxe.commons.extensions.inflate
import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.features.recipedetail.RecipeDetailActivity
import com.tuproject.foodeluxe.commons.adapter.recipelist.RecipeListAdapter
import com.tuproject.foodeluxe.commons.adapter.RecyclerItemTouchHelper
import com.tuproject.foodeluxe.commons.adapter.SwipeActionType
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.recipe_search_fragment.*
import javax.inject.Inject

@ActivityScoped
class RecipeSearchFragment @Inject constructor() : DaggerFragment(), RecipeSearchContract.View,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private var query = ""
    private var itemOnClick: (View, Int, Int) -> Unit = { view, position, type -> handleClick(view, position, type) }

    @Inject
    lateinit var presenter: RecipeSearchContract.Presenter

    private lateinit var recipeListAdapter: RecipeListAdapter
    private var recipeSearchResults: RecipeSearchResults? = null

    private var subscriptions = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.recipe_search_fragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        query = arguments!!.getString("query")!!
        initRecyclerView()
        initAdapter()

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_RECIPE)) {
            recipeSearchResults = savedInstanceState.get(KEY_RECIPE) as RecipeSearchResults
            (recipe_list.adapter as RecipeListAdapter).clearAndAddRecipes(recipeSearchResults!!.recipes)
        } else {
            requestRecipes()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (recipe_list != null) {
            val recipes = (recipe_list.adapter as RecipeListAdapter).getRecipes()
            if (recipeSearchResults != null && recipes.isNotEmpty()) {
                outState.putParcelable(KEY_RECIPE, recipeSearchResults?.copy(recipes = recipes))
            }
        }
    }

    private fun requestRecipes() {
        /**
         * first time we send 0 for "from" parameter.
         * Next time we will have "recipeSearchResult" set with the next page to
         * navigate with the "from" param.
         */
        Log.d("SearchFragment", "Request Recipes $query")
        val subscription = presenter.getRecipes(query)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { recipeResults ->
                    recipeSearchResults = recipeResults
                    (recipe_list.adapter as RecipeListAdapter).addRecipes(recipeResults.recipes)

                    //animate after data is fetched -> only first time -> itemcount <=11
                    if ((recipe_list.adapter as RecipeListAdapter).itemCount <= 11) {
                        runLayoutAnimation(recipe_list)
                    }

                },
                { e -> Log.e("SearchFragment", e.toString())
                }

            )
        subscriptions.add(subscription)
    }

    private fun handleClick(view: View, position: Int, type: Int) {
        Log.d("Search", position.toString())
        Log.d("Search", (recipe_list.adapter as RecipeListAdapter).getRecipes()[position].label)

        val intent = Intent(activity, RecipeDetailActivity::class.java).apply {
            putExtra("recipe_detail", (recipe_list.adapter as RecipeListAdapter).getRecipes()[position])
        }
        startActivity(intent)

    }

    private fun initRecyclerView() {
        Log.d("SearchFragment", "Init RecyclerView $query")
        recipe_list.apply {
            setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ requestRecipes() }, linearLayoutManager))
            requestFocus()
        }

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        val itemTouchHelperCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recipe_list)

    }

    private fun initAdapter() {
        if (recipe_list.adapter == null) {
            recipeListAdapter = RecipeListAdapter(
                itemOnClick,
                SwipeActionType.SAVE
            )
            recipeListAdapter.onItemClick = {
                Log.d("SearchFragment", "Item clicked")
            }
            recipe_list.adapter = recipeListAdapter
        }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_bottom)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is RecipeListAdapter.MyViewHolder) {
            // remove the item from recycler view
            val item = recipeListAdapter.getRecipes()[viewHolder.adapterPosition]
            item.isFav = true
            presenter.saveItemToRecipeDatabase(item)
            Toast.makeText(activity, item.label + " added to favourites", Toast.LENGTH_LONG).show()
            recipeListAdapter.removeItem(viewHolder.adapterPosition)
            recipeListAdapter.restoreItem(item, position)
        }
    }

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeDisposable()
        presenter.takeView(this)
        requestRecipes()

    }

    override fun onPause() {
        super.onPause()
        if (!subscriptions.isDisposed) {
            subscriptions.dispose()
        }
        subscriptions.clear()
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    companion object {
        private const val KEY_RECIPE = "recipes"
    }
}
