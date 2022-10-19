package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class TableAdapter(val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data : List<TableData> = arrayListOf()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
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