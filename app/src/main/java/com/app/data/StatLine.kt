package com.app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager
import com.app.ngn.R
import com.app.view.StatBar

data class StatLine(val name : String, val leftValue : Int, val rightValue : Int)


/**
 * Stat line list manager
 */
class StatManager(var data : List<StatLine>) : ListManager<StatLine> {
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_statistic, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    override fun getSize(): Int {
        return data.size
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