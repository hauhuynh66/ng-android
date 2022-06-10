package com.app.listener

import com.app.data.NoteData

interface NoteItemListener {
    fun onItemClick(note:NoteData)
}