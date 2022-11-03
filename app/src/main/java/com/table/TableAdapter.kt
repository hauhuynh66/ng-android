package com.table

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.app.util.Generator.Companion.generateString

class TableAdapter(private val columns : Int, private val rows : Int = 1) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data : MutableList<List<Any?>> = mutableListOf()

    init {
        for(i in 0 .. rows){
            data.add(getDefault(columns))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TableLineViewHolder(generateRow(columns, parent))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TableLineViewHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return rows
    }

    override fun getItemViewType(position: Int): Int {
        return if(position>0) 0 else 1
    }

    class TableLineViewHolder(v : View) : RecyclerView.ViewHolder (v) {
        fun bind(data : List<Any?>){
            var count = 0
            val parent = itemView as ViewGroup
            parent.children.iterator().forEach{
                (it as TextView).text = data[count]?.toString()
                count++
            }
        }
    }

    private fun generateCellIds(columns: Int) : List<Int>{
        val list = mutableListOf<Int>()
        for(i in 0 until columns){
            list.add(View.generateViewId())
        }
        return list
    }

    private fun generateRow(columns : Int, parent : ViewGroup) : ViewGroup {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.table_line_holder, parent, false)
        val holder = view.findViewById<ConstraintLayout>(R.id.line_holder)
        holder.setBackgroundResource(R.color.Aqua)
        val ids = generateCellIds(columns)
        for(i : Int in 0 until columns){
            val textView = EditText(parent.context)
            val set = ConstraintSet()
            textView.layoutParams = ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            textView.gravity = Gravity.CENTER
            textView.id = ids[i]

            holder.addView(textView)

            set.clone(holder)
            set.connect(holder.id, ConstraintSet.TOP, ids[i], ConstraintSet.TOP)
            set.connect(holder.id, ConstraintSet.BOTTOM, ids[i], ConstraintSet.BOTTOM)

            if(i == 0){
                set.connect(ids[i], ConstraintSet.LEFT, holder.id, ConstraintSet.LEFT)
                set.connect(ids[i], ConstraintSet.RIGHT, ids[i+1], ConstraintSet.LEFT)
            }
            else if(i < columns - 1){
                set.connect(ids[i], ConstraintSet.LEFT, ids[i-1], ConstraintSet.RIGHT)
                set.connect(ids[i], ConstraintSet.RIGHT, ids[i+1], ConstraintSet.LEFT)
            }
            else{
                set.connect(ids[i], ConstraintSet.LEFT, ids[i-1], ConstraintSet.RIGHT)
                set.connect(ids[i], ConstraintSet.RIGHT, holder.id, ConstraintSet.RIGHT)
            }
            set.applyTo(holder)
        }
        return holder
    }

    private fun getDefault(columns: Int) : List<Any>{
        val list = mutableListOf<Any>()
        for(i in 0 until columns){
            list.add(generateString(5))
        }
        return list
    }

    private fun exportData() : Boolean {
        try {
            return true
        }catch (e : Exception){
            return false
        }
    }
}