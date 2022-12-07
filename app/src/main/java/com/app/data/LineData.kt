package com.app.data

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

data class LineData(
    val name : String,
    val value : Any? = null,
    val icon : Int? = null,
    val option : LineDisplayOption = LineDisplayOption()){

}

class LineDisplayOption(val color : Int = Color.BLACK, val textSize : Float = 6f, val gravity: Int? = null)


class LineManager(data : List<LineData>, private val style: LineStyle) : ListManager<LineData>(data) {
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout = when(style){
            LineStyle.One->{
                R.layout.com_list_line_1
            }
            LineStyle.Two->{
                R.layout.com_list_line_2
            }
        }

        return ViewHolder(inflater.inflate(layout, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : LineData){
            itemView.findViewById<TextView>(R.id.name).apply {
                text = data.name
                if(data.option.gravity!=null){
                    gravity = data.option.gravity
                }
            }

            itemView.findViewById<TextView>(R.id.value).apply {
                text = data.value.toString()
                setTextColor(data.option.color)
                setTextSize(TypedValue.COMPLEX_UNIT_PT, data.option.textSize)

                if(data.option.gravity!=null){
                    gravity = data.option.gravity
                }
            }

            if(data.icon != null){
                itemView.findViewById<TextView>(R.id.icon).visibility = View.VISIBLE
            }
        }
    }

    enum class LineStyle{
        One,
        Two
    }
}