package com.app.listener

import com.app.data.NoteData

interface NoteDialogListener {
    fun onAdd(note: NoteData)
    fun onCancel(temp: NoteData)
}