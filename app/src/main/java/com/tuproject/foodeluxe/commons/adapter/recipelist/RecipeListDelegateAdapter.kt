package com.tuproject.foodeluxe.commons.adapter.recipelist

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.tuproject.foodeluxe.R
import com.tuproject.foodeluxe.commons.adapter.SwipeActionType
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.commons.adapter.ViewType
import com.tuproject.foodeluxe.commons.adapter.ViewTypeDelegateAdapter
import com.tuproject.foodeluxe.commons.extensions.inflate
import com.tuproject.foodeluxe.commons.extensions.loadImg
import com.tuproject.foodeluxe.commons.extensions.onClick
import kotlinx.android.synthetic.main.recipe_item.view.*
import java.lang.Exception
import kotlin.math.roundToInt


class RecipeListDelegateAdapter(private val itemClickListener: (View, Int, Int) -> Unit, val type: Int) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecipeListAdapter.MyViewHolder =
        TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecipeListAdapter.MyViewHolder, item: ViewType) {
        holder as TurnsViewHolder
        holder.onClick(itemClickListener)
        try {
            holder.bind(item as RecipeItem, type)
        } catch (e: Exception) {
            Log.e("RecipeListDelegateA", e.toString())}

    }

    class TurnsViewHolder(parent: ViewGroup) :
        RecipeListAdapter.MyViewHolder(view = parent.inflate(R.layout.recipe_item)) {

        @SuppressLint("SetTextI18n")
        fun bind(item: RecipeItem, type: Int) = with(itemView) {

            img_thumbnail.loadImg(item.thumbnail)
            label.text = item.label
            description.text = "by " + item.source

            if (item.totalTime != 0) {
                cooking_time.text = item.totalTime.toString() + " min"
            }
            else {
                cooking_time.visibility = View.GONE
            }

            calories.text = (item.calories/item.yield).roundToInt().toString() + " kcal"

            if (item.dietLabels != emptyList<String>()) {
                val dietLabels = StringBuilder()
                for (dietLabel in item.dietLabels) dietLabels.append(dietLabel).append("   ")
                diet_labels.text = dietLabels
            }
            else {
                diet_labels.visibility = View.GONE
            }

            if (item.healthLabels != emptyList<String>()) {
                val healthLabels = StringBuilder()
                for (healthLabel in item.healthLabels) healthLabels.append(healthLabel).append("   ")
                health_labels.text = healthLabels
            }
            else {
                health_labels.visibility = View.GONE
            }

            when(type) {
                SwipeActionType.SAVE -> {
                    view_background_save.visibility = View.VISIBLE
                    view_background_delete.visibility = View.GONE
                }
                SwipeActionType.DELETE -> {
                    view_background_save.visibility = View.GONE
                    view_background_delete.visibility = View.VISIBLE
                }
            }

        }
    }
}