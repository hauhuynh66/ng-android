package com.app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class DrawUtilAdapter(val context : Context, val data : ArrayList<DrawUtilData>, val type : Int, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(type){
            0->{
                ColorHolder(inflater.inflate(R.layout.com_draw_color, parent, false))
            }
            else->{
                LineHolder(inflater.inflate(R.layout.com_draw_path, parent, false))
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
            val btn = v.findViewById<Button>(R.id.com_draw_color_action)
            btn.setBackgroundColor(data.value)
            btn.setOnClickListener {
                listener.onClick(data.value)
            }
        }
    }

    class LineHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener, context: Context){
            val sz = v.findViewById<TextView>(R.id.com_draw_path_action)
            sz.text = data.value.toString()
            sz.setOnClickListener {
                listener.onClick(data.value)
            }
        }
    }

    interface Listener{
        fun onClick(value : Int)
    }

    open class DrawUtilData(val value : Int, val selected : Boolean = false)
}