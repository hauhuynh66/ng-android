package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FootballResult
import com.app.data.FootballTeam
import com.app.ngn.R

class FootballFixtureAdapter(val context : Context, var data : ArrayList<FootballResult>, val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return FootballViewHolder(inflater.inflate(R.layout.com_football_result, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FootballViewHolder).bind(data[position], callback)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class FootballViewHolder(v : View) : RecyclerView.ViewHolder(v){

        fun bind(data : FootballResult, callback: Callback){
            itemView.setOnClickListener {
                callback.onClick(data)
            }

            itemView.findViewById<ImageView>(R.id.team_icon).apply {
                setOnClickListener {
                    callback.onTeamClick(data.homeTeam)
                }
                setImageBitmap(null)
            }

            itemView.findViewById<ImageView>(R.id.team_icon2).apply {
                setOnClickListener {
                    callback.onTeamClick(data.awayTeam)
                }
                setImageBitmap(null)
            }

            itemView.findViewById<TextView>(R.id.score1).apply {
                text = if(data.homeGoal!=null){
                    data.homeGoal.toString()
                }else{
                    "_"
                }
            }

            itemView.findViewById<TextView>(R.id.score2).apply {
                text = if(data.awayGoal!=null){
                    data.awayGoal.toString()
                }else{
                    "_"
                }
            }

            itemView.findViewById<TextView>(R.id.team_name).apply {
                text = data.homeTeam.name
            }

            itemView.findViewById<TextView>(R.id.team_name2).apply {
                text = data.awayTeam.name
            }

            itemView.findViewById<TextView>(R.id.referee).apply {
                text = data.referee
            }
        }
    }

    interface Callback{
        fun onTeamClick(team : FootballTeam)
        fun onClick(overview : FootballResult)
    }
}