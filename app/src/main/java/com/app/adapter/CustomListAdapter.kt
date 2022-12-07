package com.app.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.data.ListManager

class CustomListAdapter<T>(private val listManager: ListManager<T>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return listManager.createView(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        listManager.bind(holder, position)
    }

    override fun getItemCount(): Int {
        return listManager.getSize()
    }
}