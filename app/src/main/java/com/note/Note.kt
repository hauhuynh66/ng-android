package com.note

import androidx.recyclerview.widget.RecyclerView

abstract class Note(val title : String, val content : String){
    protected var onNoteClickListener : OnNoteClickListener = object : OnNoteClickListener{}
    abstract fun getViewHolder() : RecyclerView.ViewHolder
}

interface OnNoteClickListener{
    fun onClick(){

    }
}

enum class NoteType{
    Generic,
    Picture,
    Stat,
    Attach
}