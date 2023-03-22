package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.charts.Bar
import com.charts.Gauge
import com.charts.Line
import com.charts.Pie

class CardAdapter(val data : ArrayList<CardData>, val orientation : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
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
        val data : ArrayList<Number>,
        val title : String? = null,
        val subtitle : String? = null,
        val type : String
    )

    class PieChartCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){
            if(orientation == LinearLayoutManager.VERTICAL){
                itemView.findViewById<TextView>(R.id.title).text = data.title
                itemView.findViewById<TextView>(R.id.subtitle).text = data.subtitle
            }

            itemView.findViewById<Pie>(R.id.chart).apply {
                //this.setData(data.data)
            }
        }
    }

    class LineChartCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){
            if(orientation == LinearLayoutManager.VERTICAL){
                itemView.findViewById<TextView>(R.id.title).text = data.title
                itemView.findViewById<TextView>(R.id.subtitle).text = data.subtitle
            }

            itemView.findViewById<Line>(R.id.chart).apply {
                //this.setData(data.data)
            }
        }
    }

    class BarChartCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){
            if(orientation == LinearLayoutManager.VERTICAL){
                itemView.findViewById<TextView>(R.id.title).text = data.title
                itemView.findViewById<TextView>(R.id.subtitle).text = data.subtitle
            }

            itemView.findViewById<Bar>(R.id.chart).apply {
                //this.setData(data.data)
            }
        }
    }

    class GaugeCard(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : CardData, orientation: Int){
            if(orientation == LinearLayoutManager.VERTICAL){
                itemView.findViewById<TextView>(R.id.title).text = data.title
                itemView.findViewById<TextView>(R.id.subtitle).text = data.subtitle
            }

            itemView.findViewById<Gauge>(R.id.chart).apply {
                this.setData(data.data[0])
            }
        }
    }
}