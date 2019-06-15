package com.tuproject.foodeluxe.commons.adapter

import android.graphics.Canvas
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.RecyclerView
import com.tuproject.foodeluxe.commons.adapter.recipelist.RecipeListAdapter
import kotlinx.android.synthetic.main.recipe_item.view.*


class RecyclerItemTouchHelper(dragDirs: Int, swipeDirs: Int, var listener: RecyclerItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as RecipeListAdapter.MyViewHolder).itemView.view_foreground

            if (viewHolder.itemViewType == 1) {
                ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
            }
        }
    }

    override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val foregroundView = (viewHolder as RecipeListAdapter.MyViewHolder).itemView.view_foreground

        if (viewHolder.itemViewType == 1) {
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(
                c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive
            )
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as RecipeListAdapter.MyViewHolder).itemView.view_foreground
        if (viewHolder.itemViewType == 1) {
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val foregroundView = (viewHolder as RecipeListAdapter.MyViewHolder).itemView.view_foreground

        if (viewHolder.itemViewType == 1) {
            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(
                c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive
            )
        }

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder.itemViewType == 1) {
            listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
        }
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}