package com.app.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.LineData
import com.app.data.LineStyle
import com.app.ngn.R

class ListAdapter(
    private val data : List<LineData>,
    private val lineStyle : LineStyle
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LineViewHolder(when(lineStyle){
            LineStyle.Style1->{
                inflater.inflate(R.layout.com_list_line_1, parent, false)
            }
            LineStyle.Style2->{
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
        fun bind(data : LineData){
            view.findViewById<TextView>(R.id.name).apply {
                text = data.name
                if(data.option.gravity!=null){
                    gravity = data.option.gravity
                }
            }

            view.findViewById<TextView>(R.id.value).apply {
                text = data.value.toString()
                setTextColor(data.option.color)
                setTextSize(TypedValue.COMPLEX_UNIT_PT, data.option.textSize)

                if(data.option.gravity!=null){
                    gravity = data.option.gravity
                }
            }

            if(data.icon != null){
                view.findViewById<TextView>(R.id.icon).visibility = View.VISIBLE
            }
        }
    }
}