package com.app.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FootballStandingData
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner

class FootballStandingAdapter(val context: Context, var data : ArrayList<FootballStandingData?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            0->{
                FootballStandingHeader(inflater.inflate(R.layout.com_football_standing_header, parent, false))
            }
            else->{
                FootballStandingHolder(inflater.inflate(R.layout.com_football_standing, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position>0){
            (holder as FootballStandingHolder).bind(data[position]!!, context)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                0
            }
            else -> {
                1
            }
        }
    }

    class FootballStandingHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : FootballStandingData?, context: Context){
            itemView.findViewById<ImageView>(R.id.icon).apply {
                setImageBitmap(null)
            }
            val taskRunner = TaskRunner()
            data!!.apply {
                taskRunner.execute(ImageCallable(data.team.iconUrl), object : TaskRunner.Callback<Bitmap?>{
                    override fun onComplete(result: Bitmap?) {
                        itemView.findViewById<ImageView>(R.id.icon).apply {
                            setImageBitmap(result!!)
                        }
                    }
                })
                itemView.findViewById<TextView>(R.id.match_played).apply {
                    text = data.match.matchPlayed.toString()
                }
                itemView.findViewById<TextView>(R.id.win).apply {
                    text = data.match.matchWin.toString()
                }
                itemView.findViewById<TextView>(R.id.lose).apply {
                    text = data.match.matchLose.toString()
                }
                itemView.findViewById<TextView>(R.id.draw).apply {
                    text = data.match.matchDraw.toString()
                }

                itemView.findViewById<TextView>(R.id.goal_scored).apply {
                    text = data.score.scored.toString()
                }
                itemView.findViewById<TextView>(R.id.goal_conceded).apply {
                    text = data.score.conceded.toString()
                }

                itemView.findViewById<TextView>(R.id.points).apply {
                    text = data.point.toString()
                }
            }

            val formView = arrayListOf<ImageView>(
                itemView.findViewById(R.id.form1),
                itemView.findViewById(R.id.form2),
                itemView.findViewById(R.id.form3),
                itemView.findViewById(R.id.form4)
            )

            for(i in 0..3){
                val res = when(data.form[i]){
                    'W'->{
                        ContextCompat.getDrawable(context, R.drawable.ic_baseline_check)
                    }
                    'L'->{
                        ContextCompat.getDrawable(context, R.drawable.ic_baseline_close)
                    }
                    'D'->{
                        ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove)
                    }
                    else->{
                        null
                    }
                }
                formView[i].setImageDrawable(res)
            }
        }
    }

    class FootballStandingHeader(v : View) : RecyclerView.ViewHolder(v){

    }
}