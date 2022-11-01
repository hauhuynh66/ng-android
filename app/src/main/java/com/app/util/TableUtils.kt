package com.app.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.app.ngn.R

class TableUtils {
    companion object{
        fun getLine(nCols : Int, layoutId : Int, context: Context) : ViewGroup{
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(layoutId, null, false)
            val holder = view.findViewById<ConstraintLayout>(R.id.line_holder)
            val set = ConstraintSet()
            var latestId = View.generateViewId()
            for(i : Int in 0 until nCols){
                val textView = TextView(context)
                textView.id = View.generateViewId()
                textView.layoutParams = ViewGroup.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                textView.minWidth = 60
                textView.text = "$i"
                holder.addView(textView)

                /*if(i == 0){
                    set.connect(textView.id, ConstraintSet.LEFT,
                        ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
                }else{
                    set.connect(textView.id, ConstraintSet.LEFT,
                        latestId, ConstraintSet.RIGHT, 0)
                }*/

                /*if(i == nCols-1){
                    set.connect(textView.id, ConstraintSet.RIGHT,
                        ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
                }else{
                    set.connect(textView.id, ConstraintSet.RIGHT,
                        latestId, ConstraintSet.LEFT, 0)
                }*/

                /*set.connect(textView.id, ConstraintSet.TOP,
                    holder.id, ConstraintSet.TOP)
                set.connect(textView.id, ConstraintSet.BOTTOM,
                    holder.id, ConstraintSet.BOTTOM)*/
                set.clone(holder)
                set.applyTo(holder)
                latestId = View.generateViewId()
            }
            return holder
        }
    }
}