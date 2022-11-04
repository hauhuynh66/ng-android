package com.app.adapter

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
import com.app.dialog.EditDialog
import com.app.ngn.R

class DrawAdapter(var data : ArrayList<DrawUtilData>, private val listType : ListType, private val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selected = 0
    private val defaultColor = Color.parseColor("#000000")
    private val defaultValue = 10

    enum class ListType{
        Color,
        Text
    }

    internal enum class ItemRole{
        Normal,
        Selector;

        companion object{
            fun fromInt(value : Int) : ItemRole {
                return when(value){
                    0->{
                        Selector
                    }
                    else->{
                        Normal
                    }
                }
            }

            fun toInt(role : ItemRole) : Int {
                return when(role){
                    Selector->{
                        0
                    }
                    Normal->{
                        1
                    }
                }
            }
        }
    }

    init {
        data[selected].selected = true
        when(listType){
            ListType.Color->{
                data.add(DrawUtilData(defaultColor))
            }
            ListType.Text->{
                data.add(DrawUtilData(defaultValue))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(listType){
            ListType.Color->{
                when(ItemRole.fromInt(viewType)){
                    ItemRole.Normal->{
                        ColorHolder(inflater.inflate(R.layout.com_draw, parent, false))
                    }
                    else->{
                        ColorSelector(inflater.inflate(R.layout.com_draw, parent, false))
                    }
                }

            }
            else->{
                when(ItemRole.fromInt(viewType)){
                    ItemRole.Normal->{
                        LineHolder(inflater.inflate(R.layout.com_draw, parent, false))
                    }
                    else->{
                        LineSelector(inflater.inflate(R.layout.com_draw, parent, false))
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            data.size - 1->{
                ItemRole.toInt(ItemRole.Selector)
            }
            else->{
                ItemRole.toInt(ItemRole.Normal)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(listType){
            ListType.Color->{
                return when(ItemRole.fromInt(getItemViewType(position))){
                        ItemRole.Normal->{
                        (holder as ColorHolder).bind(data[position],listener, position)
                    }
                    else->{
                        (holder as ColorSelector).bind(data[position], listener, position)
                    }
                }
            }
            else->{
                return when(ItemRole.fromInt(getItemViewType(position))){
                    ItemRole.Normal->{
                        (holder as LineHolder).bind(data[position],listener, position)
                    }
                    else->{
                        (holder as LineSelector).bind(data[position],listener, position)
                    }
                }
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

    class ColorSelector(v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener, position: Int){
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

    class LineSelector(v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : DrawUtilData, listener: Listener, position: Int){
            val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)
            val text = holder.findViewById<TextView>(R.id.text)

            itemView.apply {
                setOnClickListener {
                    EditDialog(data.value, object : EditDialog.Listener<Int>{
                        override fun onConfirm(value: Int) {
                            text.text = value.toString()
                            listener.onSelectorClick(value, position)
                        }
                    }).show((context as AppCompatActivity).supportFragmentManager, "CSEL")
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
        fun onClick(value : Int, position: Int){

        }

        fun onSelectorClick(selected : Int, position: Int){

        }
    }

    open class DrawUtilData(var value : Int){
        var selected : Boolean = false
    }
}