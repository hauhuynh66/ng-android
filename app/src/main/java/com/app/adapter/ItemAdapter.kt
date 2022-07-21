package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.ItemData
import com.app.ngn.R

class ItemAdapter(val context: Context, var data:ArrayList<ItemData>, val listener: Listener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ItemViewHolder(inflater.inflate(R.layout.com_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(data[position], listener)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class ItemViewHolder(val v: View):RecyclerView.ViewHolder(v){
        fun bind(itemData: ItemData, listener: Listener){
            val name = v.findViewById<TextView>(R.id.itemName)
            val price = v.findViewById<TextView>(R.id.itemPrice)
            name.text = itemData.name
            price.text = itemData.price.toString()
            v.setOnClickListener {
                listener.onClick(itemData)
            }
        }
    }

    interface Listener{
        fun onClick(data : ItemData)
    }
}