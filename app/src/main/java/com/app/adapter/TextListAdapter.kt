package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class TextListAdapter(val context : Context, var data : ArrayList<String>, val mode : Int, val listener: Listener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return TextHolder1(inflater.inflate(R.layout.com_text, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TextHolder1).bind(data[position], listener )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class TextHolder1(val v:View) : RecyclerView.ViewHolder(v){
        fun bind(data : String, listener: Listener){
            val text = v.findViewById<TextView>(R.id.text)
            text.text = data
            text.setOnClickListener {
                listener.onClick(data)
            }
            text.setOnLongClickListener {
                listener.onLongClick(data)
                true
            }
        }
    }

    interface Listener{
        fun onClick(data: String)
        fun onLongClick(data : String)
    }
}