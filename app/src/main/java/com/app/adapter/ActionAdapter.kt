package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.ActionData
import com.app.ngn.R

class ActionAdapter(val data : List<ActionData>, val size : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ActionViewHolder(inflater.inflate(R.layout.com_misc,parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ActionViewHolder).bind(this.data[position], size)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class ActionViewHolder(v: View):RecyclerView.ViewHolder(v){
        fun bind(data: ActionData, size : Int){
            itemView.findViewById<TextView>(R.id.text).apply {
                text = data.text
            }

            itemView.findViewById<ImageView>(R.id.image).apply {
                this.layoutParams.width = (size * resources.displayMetrics.density).toInt()
                this.requestLayout()

                if(data.resource!=null){
                    setImageResource(data.resource)
                }
                setOnClickListener {
                    data.callback?.onClick()
                }
            }
        }
    }
}