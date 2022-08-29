package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FootballStandingData
import com.app.ngn.R

class FootballStandingAdapter(val context: Context, val data : ArrayList<FootballStandingData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return FootballStandingHolder(inflater.inflate(R.layout.com_football_standing, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FootballStandingHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class FootballStandingHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : FootballStandingData){

        }
    }
}