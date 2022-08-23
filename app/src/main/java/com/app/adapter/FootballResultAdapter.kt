package com.app.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FootballResult
import com.app.data.FootballTeam
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner

class FootballResultAdapter(val context : Context, var data : ArrayList<FootballResult>, val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    class FootballViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        private var result : FootballResult? = null
        fun bind(data : FootballResult, callback: Callback, ){
            if(result == null || data !=result){
                println(data.homeTeam.name)
                result = data
                val runner = TaskRunner()
                val logo1 = v.findViewById<ImageView>(R.id.team_icon)
                val logo2 = v.findViewById<ImageView>(R.id.team_icon2)

                logo1.setOnClickListener {
                    callback.onTeamClick(data.homeTeam)
                }

                logo2.setOnClickListener {
                    callback.onTeamClick(data.awayTeam)
                }

                runner.execute(ImageCallable(data.homeTeam.iconUrl), object : TaskRunner.Callback<Bitmap?>{
                    override fun onComplete(result: Bitmap?) {
                        if (result!=null){
                            logo1.setImageBitmap(result)
                        }
                    }
                })

                runner.execute(ImageCallable(data.awayTeam.iconUrl), object : TaskRunner.Callback<Bitmap?>{
                    override fun onComplete(result: Bitmap?) {
                        if (result!=null){
                            logo2.setImageBitmap(result)
                        }
                    }
                })

                v.findViewById<TextView>(R.id.score1).apply {
                    text = if(data.homeGoal!=null){
                        data.homeGoal.toString()
                    }else{
                        "_"
                    }
                }

                v.findViewById<TextView>(R.id.score2).apply {
                    text = if(data.awayGoal!=null){
                        data.awayGoal.toString()
                    }else{
                        "_"
                    }
                }

                v.findViewById<TextView>(R.id.teamName).apply {
                    text = data.homeTeam.name
                }

                v.findViewById<TextView>(R.id.teamName2).apply {
                    text = data.awayTeam.name
                }

                v.findViewById<TextView>(R.id.referee).apply {
                    text = data.referee
                }
            }
        }
    }

    interface Callback{
        fun onTeamClick(team : FootballTeam)
    }
}