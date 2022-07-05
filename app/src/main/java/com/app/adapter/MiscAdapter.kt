package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.app.data.MiscData
import com.app.ngn.R

class MiscAdapter(val context : Context, val data:ArrayList<MiscData>):RecyclerView.Adapter<MiscAdapter.MiscViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiscViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return MiscViewHolder(inflater.inflate(R.layout.com_misc,parent, false))
    }

    override fun onBindViewHolder(holder: MiscViewHolder, position: Int) {
        holder.bind(this.data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class MiscViewHolder(private val v: View):RecyclerView.ViewHolder(v){
        fun bind(data: MiscData){
            val button = v.findViewById<ImageButton>(R.id.com_misc_actionBtn)
            button.setOnClickListener{
                data.listener.onClick()
            }
            //button.setImageBitmap(data.bitmap)
        }
    }
}