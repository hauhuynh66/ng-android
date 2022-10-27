package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class TableAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data : List<TableData> = arrayListOf()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.com_row, parent, false)
        return TableRowViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class TableData(){

    }

    inner class TableRowViewHolder(v : View) : RecyclerView.ViewHolder(v){

    }
}