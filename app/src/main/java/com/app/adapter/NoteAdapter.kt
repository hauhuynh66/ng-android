package com.app.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.model.Note
import com.app.ngn.R
import com.app.util.Format.Companion.formatDate

class NoteAdapter(val context: Context,val data:List<Note>, private val callback: NoteAdapter.Callback?):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return NoteViewHolder(inflater.inflate(R.layout.com_note, parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(this.data[position], callback)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class NoteViewHolder(private val v: View):RecyclerView.ViewHolder(v){
        fun bind(note:Note, callback: Callback?){
            val title = v.findViewById<TextView>(R.id.com_note_title)
            val content = v.findViewById<TextView>(R.id.com_note_content)
            val date = v.findViewById<TextView>(R.id.com_note_displayDate)
            val del = v.findViewById<Button>(R.id.com_note_delete)
            title.text = note.title
            content.text = note.content
            date.text = formatDate(note.displayDate)
            if(callback!=null){
                v.setOnClickListener{
                    callback.onItemClick(note)
                }
            }
        }
    }

    class NoteItemShadowBuilder(v:View):View.DragShadowBuilder(v){
        private val shadow = ColorDrawable(Color.LTGRAY)
        override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
            val width: Int = view.width - 40
            val height: Int = view.height - 40
            shadow.setBounds(0, 0, width, height)
            outShadowSize!!.set(width, height)
            outShadowTouchPoint!!.set(width / 2, height / 2)
        }

        override fun onDrawShadow(canvas: Canvas?) {
            shadow.draw(canvas!!)
        }
    }

    interface Callback{
        fun onItemClick(note:Note)
    }
}