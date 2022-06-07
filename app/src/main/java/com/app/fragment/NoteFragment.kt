package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.NoteAdapter
import com.app.data.NoteData
import com.app.dialog.NewNoteDialog
import com.app.listener.NoteListener
import com.app.ngn.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class NoteFragment:Fragment(), NoteListener {
    private lateinit var fb:FloatingActionButton
    private lateinit var data:ArrayList<NoteData>
    private lateinit var adapter:NoteAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        data = arrayListOf()
        data.add(NoteData("Chapter 1", "Something", Date()))
        data.add(NoteData("Chapter 2", "Lorem", Date()))
        data.add(NoteData("Chapter 3", "Ipsum", Date()))
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val noteList = view.findViewById<RecyclerView>(R.id.noteList)
        noteList.layoutManager = layoutManager
        adapter = NoteAdapter(this.requireActivity(), data)
        noteList.adapter = adapter
        fb = view.findViewById(R.id.addBtn)
        fb.setOnClickListener {
            NewNoteDialog(this).show(requireActivity().supportFragmentManager, "NewNote")
        }
    }

    override fun onAdd(note: NoteData) {
        /**/
        this.data.add(note)
        adapter.notifyItemInserted(this.data.size - 1)
    }

    override fun onCancel(temp: NoteData) {
        /**/

    }
}