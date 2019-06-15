package com.tuproject.foodeluxe.commons.adapter.recipelist

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tuproject.foodeluxe.data.RecipeItem
import com.tuproject.foodeluxe.commons.adapter.AdapterConstants
import com.tuproject.foodeluxe.commons.adapter.ViewType
import com.tuproject.foodeluxe.commons.adapter.ViewTypeDelegateAdapter


class RecipeListAdapter(itemClickListener: (View, Int, Int) -> Unit, type: Int): RecyclerView.Adapter<RecipeListAdapter.MyViewHolder>() {

    private var items: ArrayList<ViewType>
    var onItemClick: ((ViewType) -> Unit)? = null
    private var delegateAdapters  = SparseArrayCompat<ViewTypeDelegateAdapter>()


    private val loadingItem = object : ViewType {
        override fun getViewType(): Int =
            AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(
            AdapterConstants.LOADING,
            LoadingDelegateAdapter()
        )
        delegateAdapters.put(
            AdapterConstants.RECIPE,
            RecipeListDelegateAdapter(itemClickListener, type)
        )
        items = ArrayList()
        items.add(loadingItem)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return delegateAdapters.get(viewType)!!.onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))!!.onBindViewHolder(holder, this.items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return this.items[position].getViewType()
    }

    fun addRecipes(news: List<RecipeItem>) {
        val initPosition = items.lastIndex
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1) //plus loading item
    }

    fun clearAndAddRecipes(recipes: List<RecipeItem>) {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
        items.addAll(recipes)
        items.add(loadingItem)
        notifyItemRangeInserted(0, items.size)
    }

    fun getRecipes(): List<RecipeItem> {
        return items
            .asSequence()
            .filter { it.getViewType() == AdapterConstants.RECIPE }
            .map { it as RecipeItem }
            .toList()
    }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex

    fun removeItem (position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem (item: ViewType ,position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    open class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}