package com.tuproject.foodeluxe.features.recipedetail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tuproject.foodeluxe.R
import com.tuproject.foodeluxe.commons.extensions.inflate
import com.tuproject.foodeluxe.commons.extensions.loadImg
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.di.ActivityScoped
import com.tuproject.foodeluxe.features.recipedetail.adapter.IngredientAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.recipe_detail_fragment.*
import javax.inject.Inject
import kotlin.math.roundToInt
import android.content.Intent
import android.net.Uri
import android.widget.TextView


@ActivityScoped
class RecipeDetailFragment @Inject constructor() : DaggerFragment(), RecipeDetailContract.View {

    @Inject
    lateinit var presenter: RecipeDetailContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.recipe_detail_fragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val item: RecipeItem = arguments!!.getParcelable("detail")!!
        initializeViews(item)
    }

    private fun initializeViews(item: RecipeItem) {

        img_thumbnail.loadImg(item.thumbnail)
        label.text = item.label
        description.text = item.source

        initAdapter(item.ingredientLines)

        Log.v("Fragment", item.uri)

        if (item.totalTime != 0) {
            cooking_time.text = getString(R.string.min, item.totalTime.toString())
        } else {
            cooking_time.text = "-"
        }

        favourite_btn.apply {
            //if item is in favourites
            if (item.isFav) {
                text = getString(R.string.remove_from_fav)
                setOnClickListener {
                    item.isFav = false
                    presenter.removeItemFromRecipeDatabase(item)
                    Toast.makeText(context, item.label + "removed from favourites", Toast.LENGTH_SHORT).show()
                    text = getString(R.string.add_fav)
                }
            } else {
                //if item is NOT in favourites
                text = getString(R.string.add_fav)
                setOnClickListener {
                    item.isFav = true
                    presenter.saveItemToRecipeDatabase(item)
                    Toast.makeText(context, item.label + " added to favourites", Toast.LENGTH_SHORT).show()
                    text = getString(R.string.remove_from_fav)
                }
            }
        }

        show_recipe_btn.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.url)
            startActivity(i)
        }

        calories.text = getString(R.string.kcal, (item.calories / item.yield).roundToInt().toString())

/*
        if (item.dietLabels != emptyList<String>()) {
            val dietLabels = StringBuilder()
            for (dietLabel in item.dietLabels) dietLabels.append(dietLabel).append("   ")
            diet_labels.text = dietLabels
        } else {
            diet_labels.visibility = View.GONE
        }
*/

        val textViews = listOf<TextView>(label1,label2,label3,label4,label5,label6,label7,label8,label9)
        val labels: ArrayList<String> = item.dietLabels as ArrayList<String>
        labels.addAll(item.healthLabels)
        for ((i, label) in labels.withIndex()) {
            textViews[i].text = label
        }

        for (textView in textViews) if (textView.text == "") textView.visibility = View.GONE

    }

    private fun initAdapter(ingredients: List<String>) {

        val viewAdapter: RecyclerView.Adapter<*> = IngredientAdapter(ingredients, context!!)
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        ingredient_recycler_view.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }


}
