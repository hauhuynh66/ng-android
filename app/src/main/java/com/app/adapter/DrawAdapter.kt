package com.app.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.dialog.ColorSelectorDialog
import com.app.dialog.EditDialog
import com.app.dialog.NumberEditDialog
import com.app.ngn.R

abstract class DrawAdapter<T>(var data : List<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selected = 0
    protected abstract var defaultValue : T
    abstract var onItemSelectedListener : OnItemSelectedListener<T>

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

    interface OnItemSelectedListener<T>{
        fun onClick(value : T){

        }
    }
}

class ColorAdapter(data : List<Int>) : DrawAdapter<Int>(data){
    override var defaultValue: Int = Color.BLACK
    override var onItemSelectedListener: OnItemSelectedListener<Int> = object : OnItemSelectedListener<Int>{}

    init {
        val d = this.data.toMutableList()
        d.add(defaultValue)
        this.data = d
    }

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_draw, parent, false))
    }

    override fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position], this.onItemSelectedListener, position == data.size - 1)
    }

    inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : Int, listener: OnItemSelectedListener<Int>, isSelector : Boolean){
            val holder = v.findViewById<ConstraintLayout>(R.id.holder)
            holder.setBackgroundColor(data)

            holder.setOnClickListener {
                if(!isSelector){
                    listener.onClick(data)
                }else{
                    ColorSelectorDialog(defaultValue, object : ColorSelectorDialog.Callback{
                        override fun onConfirm(color: Int) {
                            defaultValue = color
                            holder.setBackgroundColor(defaultValue)
                            listener.onClick(color)
                        }
                    }).show((itemView.context as AppCompatActivity).supportFragmentManager, "")
                }
            }
        }
    }
}

class ValueAdapter(data : List<Number>) : DrawAdapter<Number>(data){
    override var defaultValue: Number = 0
    override var onItemSelectedListener: OnItemSelectedListener<Number> = object : OnItemSelectedListener<Number>{}

    init {
        val d = this.data.toMutableList()
        d.add(defaultValue)
        this.data = d
    }

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_draw, parent, false))
    }

    override fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position], this.onItemSelectedListener, position == data.size - 1)
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : Number, listener : OnItemSelectedListener<Number>, isSelector : Boolean){
            val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)
            val display = holder.findViewById<TextView>(R.id.display)
            holder.setBackgroundColor(Color.GRAY)

            display.text = data.toString()

            holder.setOnClickListener {
                if(!isSelector){
                    listener.onClick(data)
                }else{
                    NumberEditDialog(defaultValue, object :EditDialog.Listener<Number>{
                        override fun onConfirm(value: Number) {
                            defaultValue = value
                            display.text = defaultValue.toString()
                            listener.onClick(value)
                        }
                    }).show((itemView.context as AppCompatActivity).supportFragmentManager, "")
                }
            }
        }
    }
}