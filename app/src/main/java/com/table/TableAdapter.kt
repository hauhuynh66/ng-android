package com.table

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TableAdapter(private val tableManager: TableManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        tableManager.attach(parent)
        return TableLineViewHolder(tableManager.view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TableLineViewHolder).bind()
    }

    override fun getItemCount(): Int {
        return tableManager.getRows()
    }

    override fun getItemViewType(position: Int): Int {
        return if(position>0) 0 else 1
    }

    class TableLineViewHolder(v : View) : RecyclerView.ViewHolder (v) {
        fun bind(){

        }
    }
}