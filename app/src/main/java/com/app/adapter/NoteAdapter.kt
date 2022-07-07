package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.model.Note
import com.app.ngn.R
import com.app.util.Format.Companion.formatDate

class NoteAdapter(val context: Context,val data:List<Note>, private val callback: NoteAdapter.Callback?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            0->{
                NoteViewHolder(inflater.inflate(R.layout.com_note, parent, false))
            }
            else->{
                NoteImgViewHolder(inflater.inflate(R.layout.com_note_img, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            0->{
                (holder as NoteViewHolder).bind(this.data[position], callback)
            }
            else->{
                (holder as NoteImgViewHolder).bind(this.data[position], callback)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].extra!=null){
            1
        }else{
            0
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class NoteViewHolder(private val v: View) : RecyclerView.ViewHolder(v){
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

    class NoteImgViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(note:Note, callback: Callback?) {
            val img = v.findViewById<ImageView>(R.id.com_note_img_src)

        }
    }



    interface Callback{
        fun onItemClick(note:Note)
    }
}