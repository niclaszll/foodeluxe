package com.tuproject.foodeluxe.features.startpage

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
import com.tuproject.foodeluxe.commons.extensions.inflate
import com.tuproject.foodeluxe.data.RecipeSearchResults
import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.features.recipedetail.RecipeDetailActivity
import com.tuproject.foodeluxe.commons.adapter.recipelist.RecipeListAdapter
import com.tuproject.foodeluxe.commons.adapter.RecyclerItemTouchHelper
import com.tuproject.foodeluxe.commons.adapter.SwipeActionType
import com.tuproject.foodeluxe.commons.extensions.random
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_startpage.*
import java.util.*
import javax.inject.Inject

@ActivityScoped
class StartpageFragment @Inject constructor(): DaggerFragment(), StartpageContract.View, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @Inject
    lateinit var presenter: StartpageContract.Presenter

    private var itemOnClick: (View, Int, Int) -> Unit = { view, position, type -> handleClick(view, position, type) }
    private lateinit var recipeListAdapter: RecipeListAdapter
    private var recipeSearchResults: RecipeSearchResults? = null

    private var subscriptions = CompositeDisposable()
    private var queries: List<String> = listOf("Steak", "Chilli", "Cheese")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_startpage)
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
        Log.v("Startpage", "resume called")
        requestRecipes()
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    private fun requestRecipes() {
        /**
         * first time we send 0 for "from" parameter.
         * Next time we will have "recipeSearchResult" set with the next page to
         * navigate with the "from" param.
         */
        val subscription = presenter.getRecipes(queries.random(Random())!!)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { recipeResults ->
                    recipeSearchResults = recipeResults
                    (top_pick_list.adapter as RecipeListAdapter).addRecipes(recipeResults.recipes)

                    //animate after data is fetched -> only first time -> itemcount <=11
                    if ((top_pick_list.adapter as RecipeListAdapter).itemCount <= 11) {
                        runLayoutAnimation(top_pick_list)
                    }

                },
                { e -> Log.e("StartpageFragment", e.toString())
                }

            )
        subscriptions.add(subscription)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_bottom)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun initRecyclerView() {

        top_pick_list.apply {
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
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(top_pick_list)

    }

    private fun handleClick(view: View, position: Int, type: Int) {
        Log.d("Search", position.toString())
        Log.d("Search", (top_pick_list.adapter as RecipeListAdapter).getRecipes()[position].label)

        val intent = Intent(activity, RecipeDetailActivity::class.java).apply {
            putExtra("recipe_detail", (top_pick_list.adapter as RecipeListAdapter).getRecipes()[position])
        }
        startActivity(intent)

    }

    private fun initAdapter() {
        if (top_pick_list.adapter == null) {
            recipeListAdapter = RecipeListAdapter(
                itemOnClick,
                SwipeActionType.SAVE
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
            presenter.saveItemToRecipeDatabase(item)
            Toast.makeText(activity, item.label + " added to favourites", Toast.LENGTH_LONG).show()
            recipeListAdapter.removeItem(viewHolder.adapterPosition)
            recipeListAdapter.restoreItem(item, position)
        }
    }

    companion object {
        private const val KEY_RECIPE = "recipes"
    }
}
