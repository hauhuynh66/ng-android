package com.app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.app.view.StatBar

data class StatLine(val name : String, val leftValue : Int, val rightValue : Int)

/**
 * Stat line list manager
 */
class StatManager(data : List<StatLine>) : ListManager<StatLine>(data) {
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_statistic, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : StatLine){
            itemView.findViewById<StatBar>(R.id.stat_bar).apply{
                setValue(data.leftValue, data.rightValue)
            }
            itemView.findViewById<TextView>(R.id.stat_name).apply {
                text = data.name
            }
        }
    }
}