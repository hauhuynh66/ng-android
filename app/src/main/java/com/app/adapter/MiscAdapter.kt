package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.MiscData
import com.app.ngn.R

class MiscAdapter(val context : Context, val data:ArrayList<MiscData>, val selector : Int):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return MiscViewHolder(inflater.inflate(R.layout.com_misc,parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MiscViewHolder).bind(this.data[position] , selector)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class MiscViewHolder(v: View):RecyclerView.ViewHolder(v){
        fun bind(data: MiscData, selector: Int){
            itemView.findViewById<TextView>(R.id.text).apply {
                text = data.text
            }

            itemView.findViewById<ImageView>(R.id.image).apply {
                if(selector!=1){
                    this.layoutParams.width = (60 * resources.displayMetrics.density).toInt()
                    this.requestLayout()
                }
                if(data.resource!=null){
                    setImageResource(data.resource)
                }
                setOnClickListener {
                    data.listener.onClick()
                }
            }
        }
    }
}