package com.treeview.binary

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BTreeAdapter(val manager: BTreeManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class BNodeViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(bNode: BNode){

        }
    }
}