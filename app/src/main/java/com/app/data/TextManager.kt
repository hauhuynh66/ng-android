package com.app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

/**
 * One line text only list
 */
class TextManager(data : List<String>) : ListManager<String>(data){
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_text, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position], onItemClickListener)
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        fun bind(data : String, listener: OnItemClickListener<String>?){
            itemView.setOnClickListener {
                listener?.execute(data)
            }

            itemView.findViewById<TextView>(R.id.text).apply {
                this.text = data
            }
        }
    }
}