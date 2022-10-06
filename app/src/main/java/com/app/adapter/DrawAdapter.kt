package com.app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.dialog.ColorSelectorDialog
import com.app.ngn.R

class DrawAdapter(val context : Context, var data : ArrayList<DrawUtilData>, val type : Int, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal enum class Position(val type : Int){
        Normal(1),
        Selector(2)
    }

    init {
        data.add(DrawUtilData(0))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(type){
            0->{
                when(viewType){
                    Position.Normal.type->{
                        ColorHolder(inflater.inflate(R.layout.com_draw, parent, false))
                    }
                    else->{
                        ColorSelectorHolder(inflater.inflate(R.layout.com_draw, parent, false))
                    }
                }

            }
            else->{
                LineHolder(inflater.inflate(R.layout.com_draw, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            data.size - 1->{
                Position.Selector.type
            }
            else->{
                Position.Normal.type
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(type){
            0->{
                return when(getItemViewType(position)){
                    Position.Normal.type->{
                        (holder as ColorHolder).bind(data[position],listener)
                    }
                    else->{
                        (holder as ColorSelectorHolder).bind(data[position], listener, context)
                    }
                }
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
        fun bind(data : DrawUtilData, listener: Listener){
            val holder = v.findViewById<ConstraintLayout>(R.id.holder)
            if(data.selected){
                holder.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg1)
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

    class ColorSelectorHolder(v: View) : RecyclerView.ViewHolder(v){
        private var currentColor = "#ffffff"
        fun bind(data : DrawUtilData, listener: Listener, context: Context){
            val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)
            itemView.findViewById<TextView>(R.id.display).apply {
                setBackgroundColor(Color.parseColor(currentColor))
            }
            itemView.apply {
                setOnClickListener {
                    ColorSelectorDialog(object : ColorSelectorDialog.Callback{
                        override fun onConfirm(color: String) {
                            currentColor = color
                            listener.onSelectorClick(Color.parseColor(currentColor))
                        }
                    }).show((context as AppCompatActivity).supportFragmentManager, "CSEL")
                }
            }
            if(data.selected){
                holder.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg1)
            }
        }
    }

    class LineHolder(v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener){
            val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)
            if(data.selected){
                holder.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg1)
            }else{
                holder.setBackgroundResource(0)
            }
            itemView.findViewById<TextView>(R.id.display).apply{
                setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green_1))
                text = data.value.toString()
                setOnClickListener {
                    listener.onClick(data.value)
                }
            }
        }
    }

    interface Listener{
        fun onClick(value : Int)
        fun onSelectorClick(selected : Int){

        }
    }

    open class DrawUtilData(val value : Int, var selected : Boolean = false)
}