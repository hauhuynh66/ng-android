package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class DrawAdapter(val context : Context, var data : ArrayList<DrawUtilData>, val type : Int, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(type){
            0->{
                ColorHolder(inflater.inflate(R.layout.com_draw, parent, false))
            }
            else->{
                LineHolder(inflater.inflate(R.layout.com_draw, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(type){
            0->{
                (holder as ColorHolder).bind(data[position],listener, context)
            }
            else->{
                (holder as LineHolder).bind(data[position],listener, context)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ColorHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener, context: Context){
            val holder = v.findViewById<ConstraintLayout>(R.id.holder)
            if(data.selected){
                holder.background = context.getDrawable(R.drawable.bg1)
            }else{
                holder.setBackgroundResource(0)
            }
            v.findViewById<TextView>(R.id.display).apply {
                setBackgroundColor(data.value)
                setOnClickListener {
                    listener.onClick(data.value)
                }
            }
        }
    }

    class LineHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener, context: Context){
            val holder = v.findViewById<ConstraintLayout>(R.id.holder)
            if(data.selected){
                holder.background = context.getDrawable(R.drawable.bg1)
            }else{
                holder.setBackgroundResource(0)
            }
            v.findViewById<TextView>(R.id.display).apply{
                setBackgroundColor(ContextCompat.getColor(context, R.color.green_1))
                text = data.value.toString()
                setOnClickListener {
                    listener.onClick(data.value)
                }
            }
        }
    }

    interface Listener{
        fun onClick(value : Int)
    }

    open class DrawUtilData(val value : Int, var selected : Boolean = false)
}