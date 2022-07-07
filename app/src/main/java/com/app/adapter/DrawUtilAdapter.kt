package com.app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class DrawUtilAdapter(val context : Context, val data : ArrayList<Int>, val type : Int, val listener: Listener) :
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
                (holder as ColorHolder).bind(data[position],listener)
            }
            else->{
                (holder as LineHolder).bind(data[position],listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ColorHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : Int, listener: Listener){
            val btn = v.findViewById<Button>(R.id.com_draw_color_action)
            when(data){
                Color.TRANSPARENT->{
                    btn.setBackgroundColor(Color.WHITE)
                }
                else->{
                    btn.setBackgroundColor(data)
                }
            }
            btn.setOnClickListener {
                listener.onClick(data)
            }
        }
    }

    class LineHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : Int, listener: Listener){
            val sz = v.findViewById<TextView>(R.id.com_draw_path_action)
            sz.text = data.toString()
            sz.setOnClickListener {
                listener.onClick(data)
            }
        }
    }

    interface Listener{
        fun onClick(value : Int)
    }
}