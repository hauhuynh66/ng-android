package com.app.listener

import com.app.data.NoteData

interface NoteListener {
    fun onAdd(note: NoteData)
    fun onCancel(temp: NoteData)
}