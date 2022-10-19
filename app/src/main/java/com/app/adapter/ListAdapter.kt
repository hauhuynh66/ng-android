package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.LineData
import com.app.ngn.R

class ListAdapter(val context: Context, val data : ArrayList<LineData>, private val lineStyle : Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return LineViewHolder(when(lineStyle){
            1->{
                inflater.inflate(R.layout.com_list_line_1, parent, false)
            }
            else->{
                inflater.inflate(R.layout.com_list_line_2, parent, false)
            }
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LineViewHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class LineViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        fun bind(detail : LineData){
            val propName = view.findViewById<TextView>(R.id.name)
            propName.text = detail.name
            val propValue = view.findViewById<TextView>(R.id.value)
            if(detail.value!=null){
                propValue.text = detail.value.toString()
            }
        }
    }
}