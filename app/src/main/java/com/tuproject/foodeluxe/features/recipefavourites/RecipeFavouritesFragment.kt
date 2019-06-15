package com.tuproject.foodeluxe.features.recipefavourites

import com.tuproject.foodeluxe.di.ActivityScoped
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tuproject.foodeluxe.R
import com.tuproject.foodeluxe.commons.extensions.inflate
import com.tuproject.foodeluxe.data.RecipeSearchResults
import com.tuproject.foodeluxe.features.recipedetail.RecipeDetailActivity
import com.tuproject.foodeluxe.commons.adapter.recipelist.RecipeListAdapter
import com.tuproject.foodeluxe.commons.adapter.RecyclerItemTouchHelper
import com.tuproject.foodeluxe.commons.adapter.SwipeActionType
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.recipe_favourites_fragment_.*
import javax.inject.Inject

@ActivityScoped
class RecipeFavouritesFragment @Inject constructor(): DaggerFragment(), RecipeFavouritesContract.View, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @Inject
    lateinit var presenter: RecipeFavouritesContract.Presenter

    private var itemOnClick: (View, Int, Int) -> Unit = { view, position, type -> handleClick(view, position, type) }
    private lateinit var recipeListAdapter: RecipeListAdapter
    private var recipeSearchResults: RecipeSearchResults? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.recipe_favourites_fragment_)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initAdapter()

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_RECIPE)) {
            recipeSearchResults = savedInstanceState.get(KEY_RECIPE) as RecipeSearchResults
            (top_pick_list.adapter as RecipeListAdapter).clearAndAddRecipes(recipeSearchResults!!.recipes)
        } else {
            requestRecipes()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
        (top_pick_list.adapter as RecipeListAdapter).clearAndAddRecipes(presenter.getFavourites())
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    private fun requestRecipes() {
        (top_pick_list.adapter as RecipeListAdapter).addRecipes(presenter.getFavourites())
    }

    private fun initRecyclerView() {

        top_pick_list.apply {
            setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            clearOnScrollListeners()
            requestFocus()
        }

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        val itemTouchHelperCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(top_pick_list)

    }

    private fun handleClick(view: View, position: Int, type: Int) {

        val intent = Intent(activity, RecipeDetailActivity::class.java).apply {
            putExtra("recipe_detail", (top_pick_list.adapter as RecipeListAdapter).getRecipes()[position])
        }
        startActivity(intent)

    }

    private fun initAdapter() {
        if (top_pick_list.adapter == null) {
            recipeListAdapter = RecipeListAdapter(
                itemOnClick,
                SwipeActionType.DELETE
            )
            recipeListAdapter.onItemClick = {
                Log.d("SearchFragment", "Item clicked")
            }
            top_pick_list.adapter = recipeListAdapter
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is RecipeListAdapter.MyViewHolder) {
            // remove the item from recycler view
            val item = recipeListAdapter.getRecipes()[viewHolder.adapterPosition]
            Toast.makeText(activity, item.label + " removed from favourites", Toast.LENGTH_LONG).show()
            presenter.removeFromFavourites(item)
            recipeListAdapter.removeItem(viewHolder.adapterPosition)
        }
    }

    companion object {
        private const val KEY_RECIPE = "recipes"
    }
}
