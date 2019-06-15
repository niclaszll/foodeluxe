package com.tuproject.foodeluxe.commons.adapter.recipelist

import android.view.ViewGroup
import com.tuproject.foodeluxe.R
import com.tuproject.foodeluxe.commons.adapter.ViewType
import com.tuproject.foodeluxe.commons.adapter.ViewTypeDelegateAdapter
import com.tuproject.foodeluxe.commons.extensions.inflate


class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) =
        TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecipeListAdapter.MyViewHolder, item: ViewType) {
    }

    class TurnsViewHolder(parent: ViewGroup) :
        RecipeListAdapter.MyViewHolder(parent.inflate(R.layout.recipe_item_loading))
}