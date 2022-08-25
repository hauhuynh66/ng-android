package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class SportAdapter(val context: Context, var data : ArrayList<Sport>, val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return SportViewHolder(inflater.inflate(R.layout.com_item_holder, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SportViewHolder).bind(data[position], callback)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class SportViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : Sport, callback: Callback){
            itemView.setOnClickListener {
                callback.onClick(data.name)
            }
            itemView.findViewById<TextView>(R.id.title).apply {
                text = data.name
            }
            itemView.findViewById<ImageView>(R.id.icon).apply {

            }
        }
    }

    class Sport(val name : String, val res : Int? = null, val des : String? = null)

    interface Callback{
        fun onClick(sportName : String)
    }
}