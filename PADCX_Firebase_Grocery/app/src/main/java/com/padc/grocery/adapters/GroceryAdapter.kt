package com.padc.grocery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.padc.grocery.R
import com.padc.grocery.data.vos.GroceryVO
import com.padc.grocery.delegates.GroceryViewItemActionDelegate
import com.padc.grocery.viewholders.GroceryGridViewHolder
import com.padc.grocery.viewholders.GroceryViewHolder
import com.zg.burgerjoint.viewholders.BaseViewHolder

class GroceryAdapter(private val mDelegate: GroceryViewItemActionDelegate,private val layoutType:Int) : BaseRecyclerAdapter<BaseViewHolder<GroceryVO>, GroceryVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<GroceryVO> {
        var view : View? = null
        when(layoutType) {
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item,parent,false)
                return GroceryViewHolder(view,mDelegate)
            }
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_grid_item,parent,false)
                return GroceryGridViewHolder(view,mDelegate)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item,parent,false)
                return GroceryViewHolder(view,mDelegate)
            }
        }
    }
}