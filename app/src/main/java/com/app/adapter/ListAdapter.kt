package com.app.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.LineData
import com.app.data.StatLine
import com.app.ngn.R
import com.app.view.StatBar

/**
 * List Manager
 */
interface ListManager<T>{
    fun createView(parent : ViewGroup) : RecyclerView.ViewHolder

    fun bind(holder: RecyclerView.ViewHolder, position: Int)

    fun getSize() : Int
}

/**
 * One line text only list
 */
class TextManager(var data : List<String>) : ListManager<String>{
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_text, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    override fun getSize(): Int {
        return data.size
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        fun bind(data : String){
            itemView.findViewById<TextView>(R.id.text).apply {
                this.text = data
            }
        }
    }
}

class ListAdapter<T>(private val listManager: ListManager<T>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return listManager.createView(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        listManager.bind(holder, position)
    }

    override fun getItemCount(): Int {
        return listManager.getSize()
    }



    interface OnItemClickListener{
        fun execute()
    }

    interface OnItemLongClickListener{
        fun execute()
    }
}