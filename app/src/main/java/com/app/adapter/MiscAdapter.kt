package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.app.data.MiscData
import com.app.ngn.R

class MiscAdapter(val context : Context, val data:ArrayList<MiscData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return MiscViewHolder(inflater.inflate(R.layout.com_misc,parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MiscViewHolder).bind(this.data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class MiscViewHolder(private val v: View):RecyclerView.ViewHolder(v){
        fun bind(data: MiscData){
            val button = v.findViewById<Button>(R.id.com_misc_actionBtn)
            button.setOnClickListener{
                data.listener.onClick()
            }
            if(data.resource!=null){
                button.setBackgroundResource(data.resource)
            }
        }
    }
}