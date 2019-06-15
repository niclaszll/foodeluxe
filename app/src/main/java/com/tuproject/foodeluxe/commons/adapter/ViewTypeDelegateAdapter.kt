package com.tuproject.foodeluxe.commons.adapter

import android.view.ViewGroup
import com.tuproject.foodeluxe.commons.adapter.recipelist.RecipeListAdapter


interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup): RecipeListAdapter.MyViewHolder

    fun onBindViewHolder(holder: RecipeListAdapter.MyViewHolder, item: ViewType)
}