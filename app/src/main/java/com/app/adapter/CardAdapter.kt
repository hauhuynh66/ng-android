package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class CardAdapter(val context : Context, val data : ArrayList<CardData>, val orientation : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            0->{
                val v = if (orientation == LinearLayoutManager.VERTICAL){
                    inflater.inflate(R.layout.pie_chart_card, parent, false)
                }else{
                    inflater.inflate(R.layout.horizontal_pie_chart_card, parent, false)
                }
                PieChartCard(v)
            }
            1->{
                val v = if (orientation == LinearLayoutManager.VERTICAL){
                    inflater.inflate(R.layout.bar_chart_card, parent, false)
                }else{
                    inflater.inflate(R.layout.horizontal_bar_chart_card, parent, false)
                }
                BarChartCard(v)
            }
            2->{
                val v = if (orientation == LinearLayoutManager.VERTICAL){
                    inflater.inflate(R.layout.line_chart_card, parent, false)
                }else{
                    inflater.inflate(R.layout.horizontal_line_chart_card, parent, false)
                }
                LineChartCard(v)
            }
            else->{
                val v = if (orientation == LinearLayoutManager.VERTICAL){
                    inflater.inflate(R.layout.gauge_card, parent, false)
                }else{
                    inflater.inflate(R.layout.horizontal_gauge_card, parent, false)
                }
                GaugeCard(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            0->{
                (holder as PieChartCard).bind(data[position], orientation)
            }
            1->{
                (holder as BarChartCard).bind(data[position], orientation)
            }
            2->{
                (holder as LineChartCard).bind(data[position], orientation)
            }
            else->{
                (holder as GaugeCard).bind(data[position], orientation)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position].type){
            "PIE"->{
                0
            }
            "BAR"->{
                1
            }
            "LINE"->{
                2
            }
            else->{
                99
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    data class CardData(
        val data : Any,
        val title : String,
        val subtitle : String? = null,
        val type : String
    )

    class PieChartCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){

        }
    }

    class LineChartCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){

        }
    }

    class BarChartCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){

        }
    }

    class GaugeCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){

        }
    }
}