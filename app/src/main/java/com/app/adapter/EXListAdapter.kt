package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FileData
import com.app.ngn.R

class EXListAdapter(val context: Activity, val data: ArrayList<FileData>) : RecyclerView.Adapter<EXListAdapter.EXListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EXListViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return EXListViewHolder(inflater.inflate(R.layout.com_ex_list,parent, false))
    }

    override fun onBindViewHolder(holder: EXListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class EXListViewHolder(val v: View) : RecyclerView.ViewHolder (v){
        fun bind(fileData: FileData){
            v.setOnClickListener{
                fileData.listener.onClick(fileData.path)
            }
            val name = v.findViewById<TextView>(R.id.com_ex_list_name)
            val size = v.findViewById<TextView>(R.id.com_ex_list_size)
            name.text = fileData.name
            size.text = fileData.size.toString()

        }
    }
}