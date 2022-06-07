package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.NoteData
import com.app.ngn.R
import com.app.util.ViewUtils.Companion.formatDate

class NoteAdapter(val context: Activity,val data:ArrayList<NoteData>):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return NoteViewHolder(inflater.inflate(R.layout.com_note, parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val title = holder.v.findViewById<TextView>(R.id.title)
        val content = holder.v.findViewById<TextView>(R.id.content)
        val date = holder.v.findViewById<TextView>(R.id.displayDate)
        title.text = this.data[position].title
        content.text = this.data[position].content
        date.text = formatDate(this.data[position].displayDate)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    public class NoteViewHolder(val v: View):RecyclerView.ViewHolder(v){
    }
}