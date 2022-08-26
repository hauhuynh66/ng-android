package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.StatLineData
import com.app.ngn.R
import com.app.view.StatBar

class StatAdapter(val context : Context, var data : ArrayList<StatLineData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return StatLineViewHolder(inflater.inflate(R.layout.com_statistic, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StatLineViewHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class StatLineViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : StatLineData){
            itemView.findViewById<StatBar>(R.id.stat_bar).apply{
                setValue(data.leftValue, data.rightValue)
            }
            itemView.findViewById<TextView>(R.id.stat_name).apply {
                text = data.name
            }
        }
    }
}