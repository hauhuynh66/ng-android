package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.app.adapter.NoteAdapter
import com.app.dialog.NoteDialog
import com.app.helper.OneColumnListHelperCallback
import com.app.listener.NoteDialogListener
import com.app.model.AppDatabase
import com.app.model.Note
import com.app.ngn.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteFragment:Fragment(), NoteDialogListener {
    private lateinit var fb:FloatingActionButton
    private lateinit var adapter:NoteAdapter
    private lateinit var data:ArrayList<Note>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val noteList = view.findViewById<RecyclerView>(R.id.noteList)
        noteList.layoutManager = layoutManager
        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "db").build()
        val d = db.noteDAO().getAll().value
        if(d!=null){
            this.data = ArrayList(d)
        }else{
            this.data = arrayListOf()
        }
        adapter = NoteAdapter(this.requireActivity(), data, null)
        noteList.adapter = adapter
        fb = view.findViewById(R.id.addBtn)
        fb.setOnClickListener {
            NoteDialog(this, null).show(requireActivity().supportFragmentManager, "NewNote")
        }
        val itemCallback = OneColumnListHelperCallback(adapter)
        val itemHelper = ItemTouchHelper(itemCallback)
        itemHelper.attachToRecyclerView(noteList)
    }

    override fun onAdd(note: Note) {
        /**/
        this.data.add(note)
        adapter.notifyItemInserted(this.data.size - 1)
    }

    override fun onCancel(temp: Note) {
        /**/

    }
}