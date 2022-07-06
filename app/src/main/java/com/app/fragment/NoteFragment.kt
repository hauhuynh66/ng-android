package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NoteFragment:Fragment(), NoteDialogListener {
    private lateinit var fb:FloatingActionButton
    private lateinit var adapter:NoteAdapter
    private lateinit var data:ArrayList<Note>
    private lateinit var db: AppDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val noteList = view.findViewById<RecyclerView>(R.id.noteList)
        noteList.layoutManager = layoutManager
        db = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()

        runBlocking {
            withContext(Dispatchers.IO){
                val d = db.noteDAO().getAll()
                println(d.size)
                data = d.toCollection(java.util.ArrayList())
                println(data.size)
            }
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
        try{
            runBlocking {
                withContext(Dispatchers.IO){
                    db.noteDAO().insert(note)
                }
            }
            this.data.add(note)
            adapter.notifyItemInserted(this.data.size - 1)
        }catch (e : Exception){
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCancel(temp: Note) {
        /**/
    }
}