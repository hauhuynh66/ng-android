package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.DetailData
import com.app.ngn.R

class DetailAdapter(val context: Context, val data : ArrayList<DetailData>, private val mode : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return DetailViewHolder(when(mode){
            1->{
                inflater.inflate(R.layout.com_detail, parent, false)
            }
            else->{
                inflater.inflate(R.layout.com_detail2, parent, false)
            }
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DetailViewHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class DetailViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        fun bind(detail : DetailData){
            val propName = view.findViewById<TextView>(R.id.name)
            propName.text = detail.name
            val propValue = view.findViewById<TextView>(R.id.value)
            if(detail.value!=null){
                propValue.text = detail.value.toString()
            }
        }
    }
}