package com.app.adapter

import android.content.Context
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.model.Note
import com.app.ngn.R
import com.app.util.Formatter.Companion.formatDate
import java.io.File

class NoteAdapter(val context: Context, val data: MutableList<Note>, private val callback: Callback?)
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
                (holder as NoteImgViewHolder).bind(this.data[position], callback, context)
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
        fun bind(note: Note, callback: Callback?){
            val title = v.findViewById<TextView>(R.id.title)
            val content = v.findViewById<TextView>(R.id.body_text)

            title.text = note.title
            content.text = note.content
            if(callback!=null){
                v.setOnClickListener{
                    callback.onItemClick(note)
                }
            }
        }
    }

    class NoteImgViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(note: Note, callback: Callback?, context: Context) {
            val img = v.findViewById<ImageView>(R.id.com_note_img_src)
            val file = File(note.extra!!)
            if(file.exists()){
                val uri = FileProvider.getUriForFile(context, "com.app.activity.ngn", File(note.extra!!))
                img.setImageBitmap(MediaStore.Images.Media.getBitmap(context.contentResolver, uri))
            }
            if(callback!=null){
                v.setOnClickListener{
                    callback.onItemClick(note)
                }
            }
        }
    }

    fun removeItem(position: Int) : Note{
        val item = data[position]
        data.remove(item)
        this.notifyItemRemoved(position)
        return item
    }

    fun restoreItem(item : Note, position: Int){
        data.add(position, item)
        this.notifyItemInserted(position)
    }

    interface Callback{
        fun onItemClick(note: Note)
    }
}