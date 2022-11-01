package com.app.adapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.app.util.TableUtils

class TableAdapter(private val cols : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val h = 5
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TableLineViewHolder(TableUtils.getLine(cols, R.layout.table_line_holder, parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TableLineViewHolder).bind()
    }

    override fun getItemCount(): Int {
        return h
    }

    class TableLineViewHolder(v : View) : RecyclerView.ViewHolder (v) {
        fun bind(){
            val parent = itemView.findViewById<ConstraintLayout>(R.id.line_holder)
        }
    }
}