package com.app.data.explorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager
import com.app.ngn.R

class FileInfoManager(data : List<FileInfo>, private val type: Type) : ListManager<FileInfo>(data){
    private val displayData : MutableList<FileDisplay> = mutableListOf()
    private var currentItemMode : ItemMode = ItemMode.Select

    enum class Type{
        Grid,
        Line
    }

    enum class ItemMode{
        Select,
        Display
    }

    init {
        data.forEach {
            displayData.add(FileDisplay(it))
        }
    }

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(type){
            Type.Grid ->{
                GridView(inflater.inflate(R.layout.com_ex_grid, parent, false))
            }
            Type.Line ->{
                LineView(inflater.inflate(R.layout.com_ex_line, parent, false))
            }
        }
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        when(type){
            Type.Grid ->{
                (holder as GridView).bind()
            }
            Type.Line ->{
                (holder as LineView).bind()
            }
        }
    }

    inner class LineView(v : View) : RecyclerView.ViewHolder(v) {
        fun bind(){

        }
    }

    inner class GridView(v : View) : RecyclerView.ViewHolder(v) {
        fun bind(){

        }
    }
}