package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class ExpandableCardAdapter(val context: Activity, val data : ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val expandPos: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardViewHolder((context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.com_search_card, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val isExpanded = position == expandPos

    }

    override fun getItemCount(): Int {
        return 1
    }

    class CardViewHolder(private val v : View) : RecyclerView.ViewHolder(v){
        fun bind(){

        }
    }
}