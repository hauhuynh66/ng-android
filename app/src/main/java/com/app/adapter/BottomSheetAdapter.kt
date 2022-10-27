package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.BottomSheetActionData
import com.app.ngn.R

class BottomSheetAdapter(val data : List<BottomSheetActionData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ActionHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.com_sheet_element, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ActionHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ActionHolder(v: View) : RecyclerView.ViewHolder(v){
        fun bind(action : BottomSheetActionData){
            itemView.findViewById<TextView>(R.id.action_name).apply {
                text = action.name
                if(action.isEnabled){
                    setOnClickListener {

                    }
                }
            }

        }
    }
}