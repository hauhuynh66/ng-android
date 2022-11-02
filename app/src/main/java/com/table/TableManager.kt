package com.table

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.app.ngn.R

class TableManager(private val columns: Int) {
    lateinit var view : View
    private var parent : ViewGroup? = null
    private val table = Table(columns, listOf(
        RowData(5,7,12,4,12),

    ))

    fun getRows() : Int{
        return 5
    }

    fun attach(parent: ViewGroup){
        this.parent = parent
        view = generateLine(columns, parent)
    }

    private fun generateLine(columns : Int, parent : ViewGroup) : View {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.table_line_holder, parent, false)
        val holder = view.findViewById<ConstraintLayout>(R.id.line_holder)
        val ids = arrayListOf<Int>()

        for(i : Int in 0 until columns){
            val textView = TextView(parent.context)
            textView.id = View.generateViewId()
            textView.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            textView.gravity = Gravity.CENTER
            textView.text = "$i"

            holder.addView(textView)
            ids.add(textView.id)
        }

        for (i : Int in 0 until columns){
            val set = ConstraintSet()

            set.clone(holder)

            set.connect(holder.id, ConstraintSet.TOP, ids[i], ConstraintSet.TOP)
            set.connect(holder.id, ConstraintSet.BOTTOM, ids[i], ConstraintSet.BOTTOM)

            if(i == 0){
                set.connect(ids[i], ConstraintSet.LEFT, holder.id, ConstraintSet.LEFT)
                set.connect(ids[i], ConstraintSet.RIGHT, ids[i+1], ConstraintSet.LEFT)
            }else if(i < columns - 1){
                set.connect(ids[i], ConstraintSet.LEFT, ids[i-1], ConstraintSet.RIGHT)
                set.connect(ids[i], ConstraintSet.RIGHT, ids[i+1], ConstraintSet.LEFT)
            }else{
                set.connect(ids[i], ConstraintSet.LEFT, ids[i-1], ConstraintSet.RIGHT)
                set.connect(ids[i], ConstraintSet.RIGHT, holder.id, ConstraintSet.RIGHT)
            }

            set.applyTo(holder)
        }
        return holder
    }
}