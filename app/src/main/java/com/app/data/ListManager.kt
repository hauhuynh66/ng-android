package com.app.data

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