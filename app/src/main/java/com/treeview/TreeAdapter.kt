package com.treeview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class TreeAdapter(private val nm : NodeManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NodeHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.com_list_line_1, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        
    }

    override fun getItemCount(): Int {
        return nm.rootNodes.size
    }

    class NodeHolder(view : View) : RecyclerView.ViewHolder(view){

    }
}