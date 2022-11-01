package com.treeview.single

import androidx.recyclerview.widget.RecyclerView

interface NodeFactory {
    fun getViewHolder(id : Int) : RecyclerView.ViewHolder
}

