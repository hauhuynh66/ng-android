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

class DrawAdapter(val context : Context, var data : ArrayList<DrawUtilData>, private val listType : ListType, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selected = 0
    private val defaultColor = Color.parseColor("#000000")
    private val defaultValue = 10

    enum class ListType(val type : Int){
        Color(1),
        Value(2)
    }

    internal enum class ItemRole(val type : Int){
        Normal(1),
        Selector(2)
    }

    init {
        data[selected].selected = true
        when(listType){
            ListType.Color->{
                data.add(DrawUtilData(defaultColor))
            }
            ListType.Value->{
                data.add(DrawUtilData(defaultValue))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(listType){
            ListType.Color->{
                when(viewType){
                    ItemRole.Normal.type->{
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
                ItemRole.Selector.type
            }
            else->{
                ItemRole.Normal.type
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(listType){
            ListType.Color->{
                return when(getItemViewType(position)){
                    ItemRole.Normal.type->{
                        (holder as ColorHolder).bind(data[position],listener, position)
                    }
                    else->{
                        (holder as ColorSelectorHolder).bind(data[position], listener, context, position)
                    }
                }
            }
            else->{
                (holder as LineHolder).bind(data[position],listener, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ColorHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener, position: Int){
            val holder = v.findViewById<ConstraintLayout>(R.id.holder)
            if(data.selected){
                holder.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg1)
            }else{
                holder.setBackgroundResource(0)
            }
            v.findViewById<TextView>(R.id.display).apply {
                setBackgroundColor(data.value)
                setOnClickListener {
                    listener.onClick(data.value, position)
                }
            }
        }
    }

    class ColorSelectorHolder(v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener, context: Context, position: Int){
            val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)

            itemView.findViewById<TextView>(R.id.display).apply {
                setBackgroundColor(data.value)
            }

            itemView.apply {
                setOnClickListener {
                    ColorSelectorDialog(data.value, object : ColorSelectorDialog.Callback{
                        override fun onConfirm(color: String) {
                            listener.onSelectorClick(Color.parseColor(color), position)
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
        fun bind(data : DrawUtilData, listener: Listener, position: Int){
            val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)
            if(data.selected){
                holder.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg1)
            }else{
                holder.setBackgroundResource(0)
            }
            itemView.findViewById<TextView>(R.id.display).apply{
                setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.Green))
                text = data.value.toString()
                setOnClickListener {
                    listener.onClick(data.value, position)
                }
            }
        }
    }

    fun changeSelected(position: Int){
        this.data[selected].selected = false
        this.notifyItemChanged(selected)
        this.selected = position
        this.data[position].selected = true
        this.notifyItemChanged(position)
    }

    fun selectSelector(ret : Int, position: Int){
        this.data[position].value = ret
        changeSelected(position)
    }

    interface Listener{
        fun onClick(value : Int, position: Int)
        fun onSelectorClick(selected : Int, position: Int){

        }
    }

    open class DrawUtilData(var value : Int){
        var selected : Boolean = false
    }
}