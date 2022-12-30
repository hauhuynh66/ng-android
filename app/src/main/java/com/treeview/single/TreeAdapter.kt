package com.treeview.single

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Tree Adapter
 * nm : store list of node to for layout
 */
class TreeAdapter(private val nm : NodeManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onNodeClickListener : OnNodeClickListener = object : OnNodeClickListener {}

    fun setOnNodeClickListener(onNodeClickListener: OnNodeClickListener){
        this.onNodeClickListener = onNodeClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NodeHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NodeHolder).bind(nm.nodes[position], onNodeClickListener)
    }

    override fun getItemCount(): Int {
        return nm.nodes.size
    }

    class NodeHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(node : Node, onNodeClickListener: OnNodeClickListener){
            itemView.findViewById<TextView>(R.id.text1).text = "${node.value}"
            itemView.setPadding(
                node.level * 50,
                itemView.paddingTop,
                itemView.paddingRight,
                itemView.paddingBottom
            )
            itemView.setOnClickListener {
                onNodeClickListener.onClick(node)
            }
        }
    }

    interface OnNodeClickListener{
        fun onClick(node : Node){

        }
    }
}