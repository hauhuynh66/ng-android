package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

/**
 * List Manager
 */
abstract class ListManager<T>(val data : List<T>){
    var onItemClickListener : OnItemClickListener<T>? = null

    abstract fun createView(parent : ViewGroup) : RecyclerView.ViewHolder

    abstract fun bind(holder: RecyclerView.ViewHolder, position: Int)

    fun getSize() : Int {
        return data.size
    }

    interface OnItemClickListener<T>{
        fun execute(item : T)
    }
}

/**
 * One line text only list
 */
class TextManager(data : List<String>) : ListManager<String>(data){
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_text, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position], onItemClickListener)
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        fun bind(data : String, listener: OnItemClickListener<String>?){
            itemView.setOnClickListener {
                listener?.execute(data)
            }

            itemView.findViewById<TextView>(R.id.text).apply {
                this.text = data
            }
        }
    }
}

class CustomListAdapter<T>(private val listManager: ListManager<T>)
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
}