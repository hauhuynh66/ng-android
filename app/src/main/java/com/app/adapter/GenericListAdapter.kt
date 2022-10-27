package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class GenericListAdapter(var data : ArrayList<String>, val callback: Callback)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TextHolder(inflater.inflate(R.layout.com_text, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TextHolder).bind(data[position], callback)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class TextHolder(val v:View) : RecyclerView.ViewHolder(v){
        fun bind(data : String, callback: Callback) {
            val text = v.findViewById<TextView>(R.id.text)
            text.text = data

            itemView.setOnClickListener {
                callback.onClick(data)
            }

            itemView.setOnLongClickListener {
                callback.onLongClick(data)
                true
            }
        }
    }

    interface Callback{
        fun onClick(data: String){

        }

        fun onLongClick(data : String){

        }
    }
}