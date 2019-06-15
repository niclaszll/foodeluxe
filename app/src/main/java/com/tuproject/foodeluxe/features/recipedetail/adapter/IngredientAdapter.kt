package com.tuproject.foodeluxe.features.recipedetail.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tuproject.foodeluxe.R
import kotlinx.android.synthetic.main.ingredient_list_item.view.*


class IngredientAdapter(private val ingredients: List<String>, val context: Context) :
    RecyclerView.Adapter<IngredientAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.ingredient_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.ingredient_line.text = ingredients[position]
    }

    override fun getItemCount() = ingredients.size

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}