package com.app.listener

import com.app.model.Note

interface NoteDialogListener {
    fun onAdd(note: Note)
    fun onCancel(temp: Note)
}