package com.app.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

abstract class DrawAdapter<T>(var data : List<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selected = 0
    protected abstract var defaultValue : T
    private var onItemSelectedListener : OnItemSelectedListener? = null

    abstract fun createView(parent: ViewGroup) : RecyclerView.ViewHolder

    abstract fun bindView(holder: RecyclerView.ViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createView(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindView(holder, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun changeSelected(position: Int){

    }

    interface OnItemSelectedListener{
        fun onClick(value : Any, position: Int)
    }

    open class DrawUtilData(var value : Int){
        var selected : Boolean = false
    }
}

class ColorAdapter(data : List<Int>) : DrawAdapter<Int>(data){
    override var defaultValue: Int = Color.BLACK

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_draw, parent, false))
    }

    override fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position], position)
    }

    inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : Int, position: Int){
            val holder = v.findViewById<ConstraintLayout>(R.id.holder)
            holder.setBackgroundColor(data)
        }
    }
}

class ValueAdapter(data : List<Number>) : DrawAdapter<Number>(data){
    override var defaultValue: Number = 0

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_draw, parent, false))
    }

    override fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position], position)
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : Number, position: Int){
            val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)
            val display = holder.findViewById<TextView>(R.id.display)

            display.text = data.toString()
        }
    }
}